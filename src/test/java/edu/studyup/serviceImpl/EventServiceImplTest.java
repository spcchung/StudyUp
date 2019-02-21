package edu.studyup.serviceImpl;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test 
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
						});
	}
	
	//HW 2 Test Cases 
	
	@Test // #1 update event name to over 20 chars
	void testUpdateEventName_Over_20_BadCase() throws StudyUpException {
		int ID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(ID, "A really long event name that has over 20 characters");
		});
	}
	
	@Test //#2 update event to exact 20 chars 
	void testUpdateEventName_MaxString_GoodCase() throws StudyUpException {
		int eventID = 1;
		String newName = "20202020202020202020";		
		eventServiceImpl.updateEventName(eventID, newName);
		assertEquals(newName, DataStorage.eventData.get(eventID).getName());
		
	}
		
	@Test // #3 uodate event name to empty string 
	void testUpdateEvent_EmptyString_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "");
		assertEquals("", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test // #5 test active event with @beforeEach event 
	void testGetActiveEvents_GoodCase() {
		eventServiceImpl.getActiveEvents();
		assertEquals(1,DataStorage.eventData.size());
	}
	
	@Test // #6 test active events with two events 
	void testGetActiveEvents_TwoEvents_GoodCase(){
		
		//create another event --> total of 2 
		Event testEvent = new Event();
		Date date0 = new Date();
		testEvent.setDate(date0);
		testEvent.setEventID(3);
		testEvent.setName("testEvent");
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
		
		eventServiceImpl.getActiveEvents();
		assertEquals(2,DataStorage.eventData.size()); 
	}
	
	@Test // #7 get past event (LOGIC ERROR) -> "Event 1" should not be a past event 
	void testGetPastEvents_GoodCase() {
		
		//one past event 
		Event testEvent = new Event();
		Date date0 = new Date(98,1,1);
		testEvent.setDate(date0);
		testEvent.setEventID(2);
		testEvent.setName("testPastEvent");
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
		int num_of_past = eventServiceImpl.getPastEvents().size();
		
		assertTrue(date0.before(new Date()));
		assertTrue(eventServiceImpl.getPastEvents().contains(testEvent));
		assertEquals(1,num_of_past);
		}
	
	@Test // #8 test get past event with new future event. Same failure as #7
	void testGetPastEvents_withFutureEvent_GoodCase() {
		
		Event testEvent = new Event();
		Date date0 = new Date(120,1,1);
		testEvent.setDate(date0);
		testEvent.setEventID(2);
		testEvent.setName("testFutureEvent");

		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
		
		int num_of_past = eventServiceImpl.getPastEvents().size();
		assertEquals(2,DataStorage.eventData.size());
		assertEquals(0,num_of_past);
	}
	
	@Test //#9 Test DeleteEvent 
	void testDeleteEvents_GoodCase() {
		Event test = new Event();
		test.setEventID(3);
		
		DataStorage.eventData.put(test.getEventID(), test);
		
		assertEquals(test,eventServiceImpl.deleteEvent(3));
	}
	
	@Test // #10 Test DeleteEvent if event is ddeleted
	void testDeleteEventsNoEvent_GoodCase() {
		eventServiceImpl.deleteEvent(1);
		assertTrue(DataStorage.eventData.size()==0);
	}
	
	@Test // #11 Test Add one student to event 
	void testAddStudentToEvent_GoodCase() throws StudyUpException {
		
		//Add Event 
		int testEventID = 25;
		Event testEvent = new Event();
		testEvent.setDate(new Date());
		testEvent.setEventID(testEventID);
		testEvent.setName("Unamed");
		
		List<Student> eventStudents = new ArrayList<>();
		testEvent.setStudents(eventStudents);
		
		//AddStudent 
		Student student0 = new Student();
		student0.setFirstName("Peter");
		student0.setLastName("Parker");
		student0.setEmail("pParker@ucdavis.edu");
		student0.setId(1);
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);

		eventServiceImpl.addStudentToEvent(student0, testEventID);
		assertTrue(DataStorage.eventData.get(testEventID).getStudents().size() == 1);
	}
	
	@Test //#12 Test add two students to event 
	void testAddStudentToEvent_AddTwoStudent_GoodCase() throws StudyUpException {
		//Add event 
		int testEventID = 25;
		Event testEvent = new Event();
		testEvent.setDate(new Date());
		testEvent.setEventID(testEventID);
		testEvent.setName("Unamed");
		
		List<Student> eventStudents = new ArrayList<>();
		testEvent.setStudents(eventStudents);
		
		//AddStudent 1
		Student student0 = new Student();
		student0.setFirstName("Peter");
		student0.setLastName("Parker");
		student0.setEmail("pParker@ucdavis.edu");
		student0.setId(0);
		
		//AddStudent 2
		Student student1 = new Student();
		student1.setFirstName("Tony");
		student1.setLastName("Stark");
		student1.setEmail("tStark@ucdavis.edu");
		student1.setId(1);
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
		
		eventServiceImpl.addStudentToEvent(student0, testEventID);
		eventServiceImpl.addStudentToEvent(student1, testEventID);
		assertEquals(2,DataStorage.eventData.get(testEventID).getStudents().size());
	}
	
	@Test // #13 Test adding three students to one event. LOGIC ERROR -> should not have over two students
	void testAddThreeStudentsToEvent_BadCase() throws StudyUpException {
		//Add event 
		int testEventID = 25;
		Event testEvent = new Event();
		testEvent.setDate(new Date());
		testEvent.setEventID(testEventID);
		testEvent.setName("Unamed");
				
		List<Student> eventStudents = new ArrayList<>();
		testEvent.setStudents(eventStudents);
				
		//AddStudent 1
		Student student0 = new Student();
		student0.setFirstName("Peter");
		student0.setLastName("Parker");
		student0.setEmail("pParker@ucdavis.edu");
		student0.setId(0);
				
		//AddStudent 2
		Student student1 = new Student();
		student1.setFirstName("Tony");
		student1.setLastName("Stark");
		student1.setEmail("tStark@ucdavis.edu");
		student1.setId(1);
				
		//AddStudent 3
		Student student2 = new Student();
		student2.setFirstName("Bruce");
		student2.setLastName("Banner");
		student2.setEmail("bBanner@ucdavis.edu");
		student2.setId(2);
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
	
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student0, testEventID);
			eventServiceImpl.addStudentToEvent(student1, testEventID);
			eventServiceImpl.addStudentToEvent(student2, testEventID);
		});	
	}
	
	@Test // #14 Add student but with null student list 
	void testAddStudentToEventNullList_GoodCase() throws StudyUpException {
		//Add event without student list 
		int testEventID = 25;
		Event testEvent = new Event();
		testEvent.setDate(new Date());
		testEvent.setEventID(testEventID);
		testEvent.setName("Unamed");
	
		//Add Student 
		Student student2 = new Student();
		student2.setFirstName("Bruce");
		student2.setLastName("Banner");
		student2.setEmail("bBanner@ucdavis.edu");
		student2.setId(2);
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
		eventServiceImpl.addStudentToEvent(student2, testEventID);
		assertEquals(testEvent.getStudents().size(),DataStorage.eventData.get(testEventID).getStudents().size());
	}
	
	@Test // #15 Add students to event with null student names
	void testAddstudentToEventNullStudentNames_GoodCase() throws StudyUpException {
		
		int testEventID = 25;
		Event testEvent = new Event();
		testEvent.setDate(new Date());
		testEvent.setEventID(testEventID);
		testEvent.setName("Unamed");
		
		List<Student> eventStudents = new ArrayList<>();
		testEvent.setStudents(eventStudents);
		
		//Add Student 
		Student student2 = new Student();
		student2.setFirstName(null);
		student2.setLastName(null);
		student2.setEmail("bBanner@ucdavis.edu");
		student2.setId(2);
		
		DataStorage.eventData.put(testEvent.getEventID(), testEvent);
		eventServiceImpl.addStudentToEvent(student2, testEventID);
		assertTrue(DataStorage.eventData.get(testEventID).getStudents().contains(student2));

	}
	
	@Test // Add students to non-existing event
	void testAddStudentToNullEvent_BadCase() throws StudyUpException {
		eventServiceImpl.deleteEvent(1);
		
		assertTrue(DataStorage.eventData.isEmpty());
		
		//Add Student 
		Student student2 = new Student();
		student2.setFirstName("Bruce");
		student2.setLastName("Banner");
		student2.setEmail("bBanner@ucdavis.edu");
		student2.setId(2);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student2, 1);
		});
	}
}
	
	


