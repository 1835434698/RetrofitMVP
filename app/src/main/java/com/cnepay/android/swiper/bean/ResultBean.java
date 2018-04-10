package com.cnepay.android.swiper.bean;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ResultBean {
    private int code;
    private String msg;
    private A data;

    @Override
    public String toString() {
        return "ResultBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    class A {
        private String des;
        private String method;
        private String url;
        private String ip;
        private Author author;

        @Override
        public String toString() {
            return "A{" +
                    "des='" + des + '\'' +
                    ", method='" + method + '\'' +
                    ", url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", author=" + author +
                    '}';
        }

        class Author {
            private String des;
            private String email;
            private String address;
            private String name;
            private String github;
            private String qq;
            private String fullname;

            @Override
            public String toString() {
                return "Author{" +
                        "des='" + des + '\'' +
                        ", email='" + email + '\'' +
                        ", address='" + address + '\'' +
                        ", name='" + name + '\'' +
                        ", github='" + github + '\'' +
                        ", qq='" + qq + '\'' +
                        ", fullname='" + fullname + '\'' +
                        '}';
            }
        }
    }
}
