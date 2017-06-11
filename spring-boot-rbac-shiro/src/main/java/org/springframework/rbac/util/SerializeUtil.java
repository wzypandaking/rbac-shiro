package org.springframework.rbac.util;

import java.io.*;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/19
 * @note: 请补充说明
 * @history:
 */
public class SerializeUtil {

    public static byte[] serialize(Serializable serialize) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(serialize);
        return byteArrayOutputStream.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream inputStream1 = new ObjectInputStream(byteArrayInputStream);
        return (T) inputStream1.readObject();
    }

    public static void main(String[] args) throws Exception {

        byte[] bytes = SerializeUtil.serialize(new A());
        A a = SerializeUtil.deserialize(bytes, A.class);
        System.out.println(a.getA());
    }

    private static class A implements Serializable{
        private String a = "1212";

        public String getA() {
            return a;
        }
    }
}