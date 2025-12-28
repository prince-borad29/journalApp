package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.Users;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

//    @ParameterizedTest
//    @ArgumentsSource(UserArgumentsProvider.class)
    public void testFindByUsername(Users user){
        assertTrue(userService.saveNewUser(user));
    }

//    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,2,4",
            "5,9,14"
    })
    public void test(int a,int b,int expected){
        assertEquals(expected,a+b,"Failed For Expected");
    }

}