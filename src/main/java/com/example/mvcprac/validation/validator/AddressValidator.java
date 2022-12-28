package com.example.mvcprac.validation.validator;//package com.example.websocket.validation;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class
//AddressValidator {
//
//    // 주소 마스킹(신주소, 구주소, 도로명 주소 숫자만 전부 마스킹)
//    public static String addressMasking(String address) throws Exception {
//
//        // 신(구)주소, 도로명 주소
//        String regex = "(([가-힣]+(\\d{1,5}|\\d{1,5}(,|.)\\d{1,5}|)+(읍|면|동|가|리))" +
//                "(^구|)((\\d{1,5}(~|-)\\d{1,5}|\\d{1,5})(가|리|)|))([ ](산(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}))|)|";
//        String newRegx = "(([가-힣]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(로|길))";
//
//        Matcher matcher = Pattern.compile(regex).matcher(address);
//        Matcher newMatcher = Pattern.compile(newRegx).matcher(address);
//        if(matcher.find()) {
//            return address.replaceAll("[0-9]", "*");
//        } else if(newMatcher.find()) {
//            return address.replaceAll("[0-9]", "*");
//        }
//        return address;
//    }
//
//}
