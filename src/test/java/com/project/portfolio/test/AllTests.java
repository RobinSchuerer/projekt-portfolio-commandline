package com.project.portfolio.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ProjectPortfolio_1T_1MH_1SP_Test.class,
		ProjectPortfolio_1T_1MP_Overflow_Test.class,
	ProjectPortfolio_1T_1MP_Test.class,
	ProjectPortfolio_1T_1P_Overflow_Test.class,
	ProjectPortfolio_1T_1PP_Test.class,
	ProjectPortfolio_1T_1PP_With_Restriction_Changed_Test.class,
	ProjectPortfolio_1T_1SP_Test.class,
	ProjectPortfolio_1T_2MP_Test.class,
	ProjectPortfolio_1T_2SP_Test.class,
	ProjectPortfolio_1T_4MP_3PP_Test.class,
	ProjectPortfolio_2T_1MP_Overflow_Test.class,
	ProjectPortfolio_2T_2MH_1SP_Test.class})
public class AllTests {

}
