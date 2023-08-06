package com.root.authservice.utils;

public final class Constants {

    public static final int OTP_TIMEOUT_IN_MINS = 5;
    public static final String EMAIL_TEMPLATE_1 = "<html><head></head><body><div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">\r\n"
            + "  <div style=\"margin:50px auto;width:70%;padding:20px 0\">\r\n"
            + "    <div style=\"border-bottom:1px solid #eee\">\r\n"
            + "      <a href=\"\" style=\"font-size:1.4em;color: #3e69fb;text-decoration:none;font-weight:600\">Quick Industry</a>\r\n"
            + "    </div>\r\n"
            + "    <p style=\"font-size:1.1em\">Hi,</p>\r\n"
            + "    <p>Use the following OTP to reset your password.</p>\r\n"
            + "    <h2 style=\"background: #3e69fb;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">";

    public static final String EMAIL_TEMPLATE_2 = "</h2>\r\n"
            + "    <p style=\"font-size:0.9em;\">Regards,<br>Quick Industry Admin Team</p>\r\n"
            + "    <hr style=\"border:none;border-top:1px solid #eee\">\r\n"
            + "    <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">\r\n"
            + "      <p>Quick Industry</p>\r\n"
            + "      <p>Mumbai</p>\r\n"
            + "    </div>\r\n"
            + "  </div>\r\n"
            + "</div></body></html>";

    public static final String INVALID_EMAIL = "INVALID_EMAIL";

}
