package club.beingsoft.restaurants.controller;

import club.beingsoft.restaurants.model.User;
import club.beingsoft.restaurants.to.UserTo;
import club.beingsoft.restaurants.util.SecurityUtil;
import club.beingsoft.restaurants.util.exception.NotFoundException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserControllerTest {
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

    @Test
    public void getAll() {
        List<UserTo> usersDb = userController.getAll();
        assertUsers(usersDb, USERS_TOS_LIST);
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
        userController.delete(USER.id());
        USER.delete();
        UserTo deletedUserTo = asTo(USER);
        UserTo deletedUserToDb = userController.get(USER.id());
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
        UserTo userToDb = userController.getByMail(USER.getEmail());
        assertUsers(userToDb, USER_TO);
    }

    @Test
    public void enable() {
        userController.enable(USER.id(), false);
        UserTo disabledUserToDb = userController.get(USER.id());
        assertUsers(disabledUserToDb, USER_TO_DISABLED);
    }

    @Test
    public void getUserNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> userController.get(NOT_FOUND_ID));
    }

    @Test
    public void getByEmailNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> userController.getByMail(NOT_FOUND_EMAIL));
    }

//    @Test
//    public void saveNullPassword() {
//        UserTo userTo = getUserToNew();
//        userTo.setPassword(null);
//        Assert.assertThrows(TransactionSystemException.class, () -> userController.create(userTo));
//    }
}