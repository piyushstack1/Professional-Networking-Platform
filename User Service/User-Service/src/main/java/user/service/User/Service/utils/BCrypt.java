package user.service.User.Service.utils;

import static org.mindrot.jbcrypt.BCrypt.*;

public class BCrypt {

public static String hash(String s){
    return hashpw(s,gensalt());
}

public static boolean match(String passwordtxt , String passwordhashed){
    return checkpw(passwordtxt,passwordhashed);
}
}
