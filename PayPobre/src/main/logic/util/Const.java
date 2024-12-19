package util;

public final class Const {
    private Const(){}

    // Account types
    public static final String COMMERCIAL = "Commercial";
    public static final String PERSONAL = "Personal";
    public static final String PENDING = "Pending";
    public static final String DONE = "Done";
    public static final String CANCELED = "Canceled";
    public static final String INSTANTANEOUS = "Instantaneous";


    // Window Dimensions
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;

    //***********************ErrorControl*****************
    //****************************************************
    public static final int e_SUCCESS = 0x0000;
    public static final int e_USER_NOT_FOUND = 0x0001;
    public static final int e_WRONG_CREDENTIALS = 0x0002;
    public static final int e_USER_ALREADY_EXISTS = 0x0003;
    public static final int e_INVALID_EMAIL = 0x0004;
    public static final int e_INVALID_CREDIT_CARD = 0x0005;

    public static final int e_SKIP_CHANGE_NAME = 0x0006;
    public static final int e_SKIP_CHANGE_EMAIl = 0x0007;

    public static final int e_SIGNUP_SUCCESSFUL = 0x0010;
    public static final int e_EMPTY_FIELDS = 0xeeee;
    public static final int e_ERROR = 0xffff;

    //  Credit Card Validator
    public static final int prefix_VISA = 4;
    public static final int prefix_MASTER = 5;
    public static final int prefix_DISCOVER = 6;
    public static final int prefix_AMERICAN_EXPRESS = 37;
    //****************************************************
    //****************************************************
}
