package com.example.mvcprac;

import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packagesOf = App.class)
public class PackageDependencyTests {
//    @ArchTest // ①
//    ArchRule archRule = classes().that().haveNameMatching(".*Repository")
//            .should().onlyHaveDependentClassesThat().haveNameMatching(".*Service");
//
//    @ArchTest // ①
//    ArchRule archRule2 = classes().that().haveNameMatching(".*Service")
//            .should().onlyHaveDependentClassesThat().haveNameMatching(".*Controller");
}
