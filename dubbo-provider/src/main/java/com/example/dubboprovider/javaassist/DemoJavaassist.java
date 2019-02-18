package com.example.dubboprovider.javaassist;

import javassist.*;

import java.io.IOException;

/**
 * 动态编译
 * dubbo 默认使用 javaassist 生成代理对象
 */
public class DemoJavaassist {

    public static void main(String[] args) throws CannotCompileException, NotFoundException, IOException {
        //创建 clazz 池
        ClassPool classPool = ClassPool.getDefault();
        //创建class
        CtClass employ = classPool.makeClass("com.dasenlin.bean.Employ");
        //为 Class 创建 属性
        CtField empId = CtField.make("private Integer empId;",employ);
        CtField empName = CtField.make("private String empName;", employ);
        CtField empAge = CtField.make("private Integer empAge;", employ);
        //添加 属性
        employ.addField(empId);
        employ.addField(empName);
        employ.addField(empAge);

        CtMethod getEmpId = CtMethod.make("public Integer getEmpId(){return empId;}", employ);
        CtMethod setEmpId = CtMethod.make("public void setEmpId(Integer empId){this.empId=empId;}", employ);

        employ.addMethod(getEmpId);
        employ.addMethod(setEmpId);
        //构造函数
        // new CtClass[]{CtClass.intType, CtClass.intType,classPool.get("java.lang.String") 参数列表
        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{CtClass.intType, CtClass.intType,classPool.get("java.lang.String")}, employ);
        ctConstructor.setBody("{this.empId=empId;this.empName=empName;this.empAge=empAge;}");

        employ.addConstructor(ctConstructor);
        employ.writeFile("C:\\Users\\Administrator\\Desktop\\myjava");

    }
}
