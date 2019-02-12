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
	
	//Start of test cases for HW 2

	@Test
	void testAddStudentsToEvents_firstNameMatch_GoodCase() throws Exception {
		int newEventID = 2;
		
		//Create new event to test
		Event event1 = new Event();
		event1.setEventID(newEventID);
		
		Student newStudent = new Student();
		newStudent.setFirstName("Mark");
		newStudent.setLastName("Appleseed");
		
		DataStorage.eventData.put(event1.getEventID(), event1);
		eventServiceImpl.addStudentToEvent(newStudent, newEventID);
		assertEquals("Mark", DataStorage.eventData.get(newEventID).getStudents().get(0).getFirstName());
	}
	
	@Test
	void testAddStudentsToEvents_tooManyStudents_badCase() throws Exception {
		int newEventID = 3;
		
		Event event2 = new Event();
		event2.setEventID(newEventID);
		
		Student student1 = new Student();
		student1.setFirstName("Jennifer");
		student1.setLastName("Nguyen");	
		
		Student student2 = new Student();
		student2.setFirstName("Michael");
		student2.setLastName("Jordan");
		
		Student student3 = new Student();
		student3.setFirstName("Justin");
		student3.setLastName("Cheng");
		
		Student student4 = new Student();
		student4.setFirstName("Mary");
		student4.setLastName("Lai");
		
		DataStorage.eventData.put(event2.getEventID(), event2);
		eventServiceImpl.addStudentToEvent(student1, newEventID);
		eventServiceImpl.addStudentToEvent(student2, newEventID);
		eventServiceImpl.addStudentToEvent(student3, newEventID);
		eventServiceImpl.addStudentToEvent(student4, newEventID);

		assertTrue(DataStorage.eventData.get(newEventID).getStudents().size() <= 3);
	}
	
	@Test
	void testAddStudent_wrongEvent_badCase() {
		
		Student newStudent = new Student();
		Assertions.assertThrows(Exception.class,()-> {
			eventServiceImpl.addStudentToEvent(newStudent, 3);
		});
	}
	
	@Test
	void testDeleteEvent_goodCase() {
		int newEventID = 4;
		Event newEvent = new Event();
		newEvent.setEventID(newEventID);
		
		DataStorage.eventData.put(newEvent.getEventID(),newEvent);
		assertEquals(newEvent,eventServiceImpl.deleteEvent(newEventID));
	
	}	
	
	@Test
	void testUpdateEventName_ExactCharacters_goodCase() {
		int eventID = 11;
		Event event11 = new Event();
		event11.setEventID(eventID);
		event11.setName("ThisEventNameIsExact");
		
		DataStorage.eventData.put(event11.getEventID(),event11);
		assertEquals()
	}
	
	@Test
	void testGetActiveEvents_checkDate_goodCase() {
		int newEventID = 5;
		Event event5 = new Event();
		event5.setEventID(newEventID);
		event5.setDate(new Date());
		
		Event event7 = new Event();
		event7.setEventID(7);
		event7.setDate(new Date());
		
		DataStorage.eventData.put(event7.getEventID(), event7);
		DataStorage.eventData.put(event5.getEventID(), event5);
		
		//Since the initial event created is also an active event
		assertEquals(2, eventServiceImpl.getActiveEvents().size());
	}
	
	@Test
	void testGetActiveEvents_pastEvents_badCase() {
		Event event6 = new Event();
		event6.setEventID(6);
		Date date = new Date(2000-01-03);
		event6.setDate(date);
		
		Event event8 = new Event();
		event8.setEventID(8);
		Date date1 = new Date(2001-01-03);
		event8.setDate(date1);
		
		DataStorage.eventData.put(event6.getEventID(), event6);
		DataStorage.eventData.put(event8.getEventID(), event8);

		assertEquals(3, eventServiceImpl.getActiveEvents().size());
	}
	
	@Test
	void testGetPastEvents_goodCase() {
		Event event9 = new Event();
		event9.setEventID(9);
		Date date = new Date(1990-1-3);
		event9.setDate(date);
		
		DataStorage.eventData.put(event9.getEventID(), event9);
		
		assertEquals(1,eventServiceImpl.getPastEvents().size());
	}
	
	@Test
	void testGetPastEvents_badCase() {
		Event event10 = new Event();
		event10.setEventID(10);
		event10.setDate(new Date());
		
		DataStorage.eventData.put(event10.getEventID(),event10);
		assertEquals(1,eventServiceImpl.getPastEvents().size());
	}
	
	@Test
	void testUpdateEventName_overCharacterLimit_badCase() throws StudyUpException {
		Event event12 = new Event();
		event12.setEventID(12);
		
		DataStorage.eventData.put(event12.getEventID(),event12);
		Assertions.assertThrows(StudyUpException.class, ()-> {
			eventServiceImpl.updateEventName(12, "This event name is longer than 20 characters");
		});
	}
	
	
}
   


	





