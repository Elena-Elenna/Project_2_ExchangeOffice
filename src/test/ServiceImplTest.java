package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import service.*;
import utils.Validator;
import utils.ValidatorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ServiceImplTest {
    UserRepository userRepository = new UserRepositoryImpl();
    CheckRepository checkRepository = new CheckRepositoryImpl();
    CourseRepository courseRepository = new CourseRepositoryImpl();
    TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    ServiceCheck serviceCheck = new ServiceCheckImpl(userRepository, checkRepository,
            courseRepository, transactionRepository );
    ServiceCourse serviceCourse = new ServiceCourseImpl(courseRepository);
    ServiceUser serviceUser = new ServiceUserImpl(userRepository,serviceCheck);


    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetUserChecks() {
        int userId = 4;
        List<Check> checks = serviceCheck.getUserChecks(userId);
        assertNotNull(checks);
        assertNotEquals(0, checks.size());
        assertFalse(checks.isEmpty());
        assertTrue(checks.get(0).getIdUser() == userId);
        assertEquals(4, checks.get(0).getIdUser());
    }

    @Test
    void testCloseCheckUser() {
        int userId = 4, checkId = 1;
        boolean result = serviceCheck.closeCheckUser(userId, checkId);
        assertTrue(result);
        assertNotNull(serviceCheck.getCheckByIdUserIdCheck(userId, checkId));
        assertNotEquals(0, serviceCheck.getCheckByIdUserIdCheck(userId, checkId).getIdCheck());
        assertFalse(checkId == 0);
        assertEquals(4, serviceCheck.getCheckByIdUserIdCheck(userId, checkId).getIdUser());
    }

    @Test
    void testGetTransactionListByIdUser() {
        int userId = 1;
        List<Transaction> transactions = serviceCheck.getTransactionListByIdUser(userId);
        assertNotNull(transactions);
        assertNotEquals(0, transactions.size());
        assertFalse(transactions.isEmpty());
        assertEquals(1, transactions.get(0).getIdUser());
        assertTrue(transactions.get(0).getIdUser() == userId);
    }

    @Test
    void testTakeMoney() {
        User user = new User("Ivan", "Ivanov", 4, "4", "4");
        int checkId = 1;
        double amount = 100.0;
        boolean result = serviceCheck.takeMoney(user, checkId, amount);
        assertTrue(result);
        assertNotNull(result);
        assertFalse(serviceCheck.getCheckByIdUserIdCheck(user.getIdUser(), checkId).getSumma() == amount);
        assertNotEquals(0, serviceCheck.getCheckByIdUserIdCheck(user.getIdUser(), checkId).getSumma());}

    @Test
    void testDepositMoney() {
        User user = new User("Ivan", "Ivanov", 4, "4", "4");
        int checkId = 1;
        double amount = 100.0;
        boolean result = serviceCheck.depositMoney(user, checkId, amount);
        assertTrue(result);
        assertFalse(serviceCheck.getCheckByIdUserIdCheck(user.getIdUser(), checkId).getSumma() == amount);
        assertNotNull(result);
        assertNotEquals(0, serviceCheck.getCheckByIdUserIdCheck(user.getIdUser(), checkId).getSumma());
    }

    @Test
    void testGetCheckByIdUserIdCheck() {
        int userId = 4, checkId = 1;
        Check check = serviceCheck.getCheckByIdUserIdCheck(userId, checkId);
        assertNotNull(check);
        assertNotEquals(0, check.getIdCheck());
        assertFalse(checkId == 0);
        assertEquals(4, check.getIdUser());
        assertTrue(check.getIdUser() == userId);
        Optional<Check> optCheck = Optional.empty();
        assertThrows(ValidatorException.class, () ->
                Validator.getCheckByIdUserIdCheck(optCheck, 1, 100)
        );
    }

    @Test
    void testTransferMoneyToUser() {
        User sender = new User("Ivan", "Ivanov", 4, "4", "4");
        User recipient = new User("John", "Doe", 5, "5", "5");
        int senderCheckId = 1, recipientCheckId = 2;
        double amount = 50.0;
        boolean result = serviceCheck.transferMoneyToUser(sender, recipient, senderCheckId, recipientCheckId, amount);
        assertTrue(result);
        assertFalse(serviceCheck.getCheckByIdUserIdCheck(sender.getIdUser(), senderCheckId).getSumma() == amount);
        assertNotEquals(0, serviceCheck.getCheckByIdUserIdCheck(sender.getIdUser(), senderCheckId).getSumma());
        assertNotNull(result);
    }

    @Test
    void testTransferMoneyToMe() {
        User user = new User("Ivan", "Ivanov", 4, "4", "4");
        int fromCheck = 1, toCheck = 2;
        double amount = 50.0;
        boolean result = serviceCheck.transferMoneyToMe(user, fromCheck, toCheck, amount);
        assertTrue(result);
        assertFalse(serviceCheck.getCheckByIdUserIdCheck(user.getIdUser(), fromCheck).getSumma() == amount);
        assertNotEquals(0, serviceCheck.getCheckByIdUserIdCheck(user.getIdUser(), fromCheck).getSumma());
        assertNotNull(result);
    }

    @Test
    void testAddCheckUser() {
        boolean result = serviceCheck.addCheckUser("USD", true, 50.0, LocalDate.now(), 1);
        assertTrue(result);
        assertNotNull(result);
        assertNotEquals(0, serviceCheck.getCheckByIdUserIdCheck(1, 1).getIdCheck());
        assertFalse(serviceCheck.getCheckByIdUserIdCheck(1, 1).getIdCheck() == 0);
        assertEquals(1, serviceCheck.getCheckByIdUserIdCheck(1, 1).getIdUser());
    }

    @Test
    void testDeleteChecksByIdUser() {
        int userId = 1;
        boolean result = serviceCheck.deleteChecksByIdUser(userId);
        assertTrue(result);
        assertNotNull(result);
    }

    @Test
    void testGetCourses() {
        List<CourseCurrency> courses = serviceCourse.getCourses();
        assertNotNull(courses);
        assertEquals(2, courses.size());
        assertTrue(courses.get(1).getCurrencyMain().equals("EUR"));
        assertFalse(courses.get(0).getCurrencyMain().equals("USD"));
        assertNotEquals(0, courses.size());
    }

    @Test
    void testGetCourseLast() {
        CourseCurrency lastCourse = serviceCourse.getCourseLast();
        assertNotNull(lastCourse);
        assertEquals("EUR", lastCourse.getCurrencyMain());
        assertNotEquals(0, lastCourse.getCurrencyMain().length());
        assertFalse(lastCourse.getCurrencyMain().isEmpty());
        assertTrue(lastCourse.getCurrencyMain().equals("EUR"));

    }

    @Test
    void testGetCurrencyMain() {
        String expectedCurrency = "EUR";
        String currency = serviceCourse.getCurrencyMain();
        assertEquals(expectedCurrency, currency);
        assertNotEquals(0, currency.length());
        assertFalse(currency.isEmpty());
        assertTrue(currency.equals("EUR"));
        assertNotNull(currency);
    }

    @Test
    void testAddNewCurrency_Failure_InvalidInputs() {
        boolean result = serviceCourse.addNewCurrency("", "", 1.0);
        assertFalse(result);
        assertNotNull(result);
        assertNotEquals(0, serviceCourse.getCourses().size());
        assertTrue(serviceCourse.getCourses().size() == 2);
        assertEquals(2, serviceCourse.getCourses().size());
    }

    @Test
    void testIsEmailValid() {
        assertTrue(serviceUser.isEmailValid("test@example.com"));
        assertFalse(serviceUser.isEmailValid("invalid-email"));
        assertNotEquals(0, serviceUser.isEmailValid("valid@example.com"));
        assertDoesNotThrow(() -> Validator.isEmailValid("valid@example.com"));
        assertThrows(ValidatorException.class, () -> Validator.isEmailValid("invalid-email"));
    }

    @Test
    void testIsPasswordValid() {
        assertTrue(serviceUser.isPasswordValid("StrongP@ss1"));
        assertFalse(serviceUser.isPasswordValid("weak"));
        assertNotEquals(0, serviceUser.isPasswordValid("StrongP@ss1"));
        assertDoesNotThrow(() -> Validator.isPasswordValid("StrongP@ss1"));
        assertThrows(ValidatorException.class, () -> Validator.isPasswordValid("weak"));
    }

    @Test
    void testLogout() {
        serviceUser.logout();
        assertNull(serviceUser.getActivUser());
        assertFalse(serviceUser.getActivUser() != null);
        assertTrue(serviceUser.getActivUser() == null);
        assertEquals(null, serviceUser.getActivUser());
        assertNotEquals(0, serviceUser.getActivUser());
    }

    @Test
    void testIsUserExistById() {
        Optional<User> emptyUser = Optional.empty();
        assertThrows(ValidatorException.class, () -> Validator.isUserExistById(emptyUser, 1));
    }

    @Test
    void testAddCourse() {
        CourseCurrency course = new CourseCurrency();
        assertEquals(course, serviceCourse.addCourse(course));
        assertNotEquals(0, serviceCourse.addCourse(course));
        assertNotNull(serviceCourse.addCourse(course));
        assertFalse(serviceCourse.addCourse(course) == null);
        assertTrue(course.equals(serviceCourse.addCourse(course)));
    }

    @Test
    void testIsEmailExist() {
        userRepository.addUser("John", "Doe", "john@example.com", "password");
        assertTrue(serviceUser.isEmailExist("john@example.com"));
        assertFalse(serviceUser.isEmailExist("notfound@example.com"));
        assertNotEquals(0, serviceUser.isEmailExist("john@example.com"));
    }

    @Test
    void testIsUserBlocked() {
        userRepository.getUserById(2);
        assertTrue(serviceUser.isUserBlocked("2"));
        assertFalse(serviceUser.isUserBlocked("1"));
        assertNotEquals(0, serviceUser.isUserBlocked("2"));
        assertEquals(true, serviceUser.isUserBlocked("2"));
    }

    @Test
    void testUserStatusUpdate() {
        userRepository.getUserById(3);
        assertTrue(serviceUser.userStatusUpdate(3, Role.ADMIN));
        assertEquals(Role.ADMIN, userRepository.getUserById(3).getRole());
        assertNotEquals(0, userRepository.getUserById(3).getRole());
    }

    @Test
    void testGetUsers() {
        userRepository.addUser("John", "Doe", "john@example.com", "password");
        userRepository.addUser("Jane", "Smith", "jane@example.com", "password");
        Map<Integer, User> users = serviceUser.getUsers();
        assertEquals(8, users.size());
        assertNotEquals(0, users.size());
        assertFalse(users.size() == 0);
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    void testUserUpdatePassword() {
        userRepository.getUserById(4);
        assertTrue(serviceUser.userUpdatePassword(4, "newPasswo1!"));
        assertEquals("newPasswo1!", userRepository.getUserById(4).getPassword());
        assertNotEquals(0, userRepository.getUserById(4).getPassword());
        assertNotNull(userRepository.getUserById(4).getPassword());
        assertFalse(userRepository.getUserById(4).getPassword() == null);
    }

    @Test
    void testDelUser() {
        userRepository.getUserById(7);
        assertNull(userRepository.getUserById(7));
        assertNotEquals(0, serviceUser.delUser(7));
        assertNotNull(serviceUser.delUser(7));
    }

    @Test
    void testLoginUser() {
        userRepository.addUser("John", "Doe", "john@example.com", "password");
        assertTrue(serviceUser.loginUser("john@example.com", "password"));
        assertNotNull(serviceUser.getActivUser());
        assertFalse(serviceUser.getActivUser() == null);
        assertNotEquals(0, serviceUser.getActivUser());
        assertEquals("John", serviceUser.getActivUser().getFirstName());
    }

    @Test
    void testRegisterUser() {
        assertTrue(serviceUser.registerUser("John", "Doe", "new@example.com", "passwP1!"));
        assertTrue(serviceUser.isEmailExist("new@example.com"));
        assertNotEquals(0, serviceUser.registerUser("John", "Doe", "new@example.com", "passwP1!"));
        assertNotNull(serviceUser.registerUser("John", "Doe", "new@example.com", "passwP1!"));
    }

    @Test
    void testIsUserPresentByEmail_registerUser() {
        Optional<User> optUser = Optional.of(new User("John", "Doe", 1, "1", "1"));
        assertThrows(ValidatorException.class, () ->
                Validator.isUserPresentByEmail_registerUser(optUser, "test@example.com")
        );
    }

    @Test
    void testGetUserById() {
        Optional<User> optUser = Optional.empty();
        assertThrows(ValidatorException.class, () ->
                Validator.getUserById(optUser, 1)
        );
    }

    @Test
    void testSummaCheck() {
        Check check = new Check("EUR", true, 100, null, null, 1, 1);
        check.setSumma(50.0);
        Optional<Check> optCheck = Optional.of(check);
        assertThrows(ValidatorException.class, () ->
                Validator.summaCheck(optCheck, 100.0)
        );
    }

    @Test
    void testIsChecksEquals() {
        assertThrows(ValidatorException.class, () ->
                Validator.isChecksEquals(1, 1)
        );
    }

    @Test
    void testIsSummaLessZero() {
        assertThrows(ValidatorException.class, () ->
                Validator.isSummaLessZero(-1.0)
        );
    }

    @Test
    void testIsCurrencyPresent() {
        assertThrows(ValidatorException.class, () ->
                Validator.isCurrencyPresent("EUR", "EUR")
        );
    }
}