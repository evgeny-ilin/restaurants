package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.UserTo;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static club.beingsoft.restaurants.MenuTestData.NOT_FOUND_ID;
import static club.beingsoft.restaurants.UserTestData.*;
import static club.beingsoft.restaurants.util.UserMatcher.assertUsers;
import static club.beingsoft.restaurants.util.UserUtil.asTo;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserControllerTest {
    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    private static MockedStatic<SecurityUtil> securityUtilMocked;
    @Autowired
    private UserController userController;

    @BeforeClass
    public static void beforeAll() {
        securityUtilMocked = Mockito.mockStatic(SecurityUtil.class);
        User user = ADMIN;
        securityUtilMocked.when(SecurityUtil::getAuthUser).thenReturn(user);
    }

    @AfterClass
    public static void close() {
        securityUtilMocked.close();
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            System.out.println("Starting test: " + description.getMethodName());
        }
    };

    @Test
    public void getAll() {
        userController.getAll();
        List<UserTo> usersDb = userController.getAll();
        assertThat(usersDb).usingElementComparatorIgnoringFields("roles", "deleteDate", "deleteUser", "editDate", "registered").isEqualTo(USERS_TOS_LIST);
    }

    @Test
    public void get() {
        UserTo userDb = userController.get(USER_TO.id());
        assertUsers(userDb, USER_TO);
    }

    @Test
    public void create() {
        USER_TO_NEW.setPassword(USER_NEW.getPassword());
        UserTo userDb = userController.create(USER_TO_NEW);
        USER_TO_NEW.setPassword(USER_NEW.getPassword());
        userDb.setPassword(USER_NEW.getPassword());
        assertUsers(userDb, USER_TO_NEW);
    }

    @Test
    public void delete() {
        userController.delete(DELETED_USER.id());
        UserTo deletedUserTo = asTo(DELETED_USER);
        UserTo deletedUserToDb = userController.get(DELETED_USER.id());
        assertUsers(deletedUserToDb, deletedUserTo);
    }

    @Test
    public void update() {
        userController.update(USER_TO_UPDATED, USER_UPDATED.id());
        UserTo updatedUserToDb = userController.get(USER_UPDATED.id());
        assertUsers(updatedUserToDb, USER_TO_UPDATED);
    }

    @Test
    public void getByMail() {
        UserTo userToDb = userController.getByMail(USER_1.getEmail());
        assertUsers(userToDb, USER_TO);
    }

    @Test
    public void enable() {
        userController.enable(ADMIN.id(), false);
        UserTo disabledUserToDb = userController.get(ADMIN.id());
        assertUsers(disabledUserToDb, ADMIN_TO_DISABLED);
    }

    @Test
    public void getUserNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> userController.get(NOT_FOUND_ID));
    }

    @Test
    public void getByEmailNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> userController.getByMail(NOT_FOUND_EMAIL));
    }
}