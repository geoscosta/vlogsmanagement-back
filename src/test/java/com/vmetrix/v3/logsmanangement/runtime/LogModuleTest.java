package com.vmetrix.v3.logsmanangement.runtime;


import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.vmetrix.v3.api.misc.dto.DTO;
import com.vmetrix.v3.logsmanagement.model.LogModule;
import com.vmetrix.v3.logsmanagement.repository.LogModuleRepository;
import com.vmetrix.v3.persistence.DatabaseConfigException;
import com.vmetrix.v3.persistence.DatabaseType;
import com.vmetrix.v3.persistence.database.DatabaseBO;
import com.vmetrix.v3.persistence.settings.DatabaseConnectionSettings;

import junit.framework.TestCase;

/**
 * The Class LogModuleTest.
 */
@RunWith(JUnitPlatform.class)
public class LogModuleTest extends TestCase {
	
	@BeforeEach
	protected void setUp(){
		
		
	}
	
	//@Test
	public void testConnectSQLServer() throws DatabaseConfigException{
		
		DatabaseConnectionSettings dbCfg = DatabaseConnectionSettings.createInstance( "vcube",
				"vcube",
				"vcube", 
				"jdbc:jtds:sqlserver://localhost:1433/vcube",
				"net.sourceforge.jtds.jdbc.Driver");

		if( DatabaseType.DBTYPE_SQLSERVER.equals( dbCfg.getDatabaseType() ) ){
			
		
			DatabaseBO dbBO = DatabaseBO.connectToDatabase( "testSQLServer" , dbCfg );
	
			List<DTO> result = dbBO.executeQuery( "select getDate() as sysdate" );
			assertNotNull(result);
			assertEquals( 1, result.size() );
	
			System.out.println( result.get(0) );

		}
		
		System.out.println( dbCfg.getDatabaseType() );

			
	}

	//@Test
	public void testConnectOracleServer() throws DatabaseConfigException{
		
		DatabaseConnectionSettings dbCfg = DatabaseConnectionSettings.createInstance( "vcube",
				"vcube",
				"vcube", 
				"jdbc:oracle:thin:@localhost:1521:ORCLCDB",
				"oracle.jdbc.OracleDriver");
	
		if( DatabaseType.DBTYPE_ORACLE.equals( dbCfg.getDatabaseType() ) ){
		
			DatabaseBO dbBO = DatabaseBO.connectToDatabase( "testOracle" , dbCfg );
	
			List<DTO> result = dbBO.executeQuery( "select sysdate from dual" );
			assertNotNull(result);
			assertEquals( 1, result.size() );
	
			System.out.println( result.get(0) );
		}

		System.out.println( dbCfg.getDatabaseType() );
	}

	//@Test
	public void testConnectServer() throws DatabaseConfigException{
	
		DatabaseBO dbBO = DatabaseBO.getMainInstance();

		assertNotNull( "Database BO cannot be null", dbBO );
		
		assertNotNull( "Database Config cannot be null", dbBO.getConfig() );
		
		
		
		System.out.println( dbBO.getConfig().getDatabaseType() );
	
		if( DatabaseType.DBTYPE_ORACLE.equals(  dbBO.getConfig().getDatabaseType() ) ){
		
	
			List<DTO> result = dbBO.executeQuery( "select sysdate from dual" );
			assertNotNull(result);
			assertEquals( 1, result.size() );
	
			System.out.println( result.get(0) );

		}else {

			List<DTO> result = dbBO.executeQuery( "select getDate() as sysdate" );
			assertNotNull(result);
			assertEquals( 1, result.size() );
	
			System.out.println( result.get(0) );

		}

	}
	
	
	
	public LogModule createNewLogModule(int tp_core, String nameModule, String txPackage, String UserName) {

		LogModule logModule = new LogModule();
    	
//		logModule.setId(2l);
		logModule.setTpCore(tp_core);
		logModule.setDateCreate(LocalDateTime.now().plusDays(1));
		logModule.setDateUpdate(LocalDateTime.now().plusDays(1));
    	
		logModule.setNameModule( nameModule );
		logModule.setTextPackage( txPackage );
   
    	
		logModule.setNameCreateUser(UserName);
		logModule.setNameUpdateUser(UserName);
    	
    	return logModule;
	}

	@Test
	public void createModuleTeste() throws DatabaseConfigException{
    	
    	LogModuleRepository logSettingRepository = new LogModuleRepository();
    	

    	logSettingRepository.save(createNewLogModule(0, "spring-boot", "org.springframework.boot", "User.Name"));	
    	
		
		/*
		TestRepository testRep = new TestRepository();
		
		TestBean bean = new TestBean();
		bean.setName( "Test" );
		bean.setDateField( new Date() );
		
		testRep.save( bean  );

		Collection<TestBean> result = testRep.findAll();
		
		assertEquals( 1, result.size() );

		Optional<TestBean> first = result.stream().findFirst();
		assertTrue( first.isPresent() );
		bean = first.get();
		assertEquals( "Test", bean.getName() );
		
		bean.setName( "Test 2" );
		testRep.update( bean );
		
		result = testRep.findAll();
		assertEquals( 1, result.size() );
		first = result.stream().findFirst();
		assertTrue( first.isPresent() );
		bean = first.get();
		assertEquals( "Test 2", bean.getName() );

		Optional<TestBean> option = testRep.findById( "id", bean.getId());
		assertFalse( !option.isPresent() );
		
		bean = option.get();
		assertNotNull( bean );

		testRep.delete( bean );
		
		result = testRep.findAll();
		assertEquals( 0, result.size() );
		*/

	}

}
