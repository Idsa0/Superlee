import com.Superlee.HR.Backend.Business.ShiftFacade;
import com.Superlee.HR.Backend.Business.WorkerFacade;
import com.Superlee.HR.Backend.Business.WorkerToSend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class WorkerTests {
    private final WorkerFacade workerFacade = WorkerFacade.getInstance().setTestMode(true);
    private final ShiftFacade shiftFacade = ShiftFacade.getInstance().setTestMode(true);

    @Before
    public void setUp() {
        workerFacade.reset(0xC0FFEE);
        shiftFacade.reset(0xC0FFEE);
    }

    @After
    public void cleanUp() {
        workerFacade.clearData();
    }

    private int addShift() {
        boolean loggedIn = workerFacade.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        int result = shiftFacade.addNewShift("2025-01-01T08:00", "2025-01-01T16:00", "Hakol BeHinam");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private boolean addWorker() {
        boolean loggedIn = workerFacade.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = workerFacade.addNewWorker("0", "Super", "Lee");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private boolean addRoleManager() {
        boolean loggedIn = workerFacade.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = workerFacade.addWorkerRole("0", "Manager");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private void login() {
        workerFacade.login("0", "0");
    }

    private boolean addSecondWorker() {
        boolean loggedIn = workerFacade.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = workerFacade.addNewWorker("1", "Avi", "Ron");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private void fakeLogin(boolean hrm) {
        workerFacade.fakeLogin(hrm, "000");
    }

    private void fakeLogout() {
        workerFacade.fakeLogout();
    }

    @Test
    public void testLoginSuccess() {
        addWorker();
        assertNotNull(workerFacade.login("0", "0"));
    }

    @Test
    public void testLoginFailureNonExisting() {
        assertThrows(NoSuchElementException.class, () -> workerFacade.login("1", "1"));
    }

    @Test
    public void testLoginFailureWrongPassword() {
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.login("0", "1"));
    }

    @Test
    public void testLoginFailureNullPassword() {
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.login("0", null));
    }

    @Test
    public void testLoginFailureNullId() {
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.login(null, "0"));
    }

    @Test
    public void testLoginFailureEmptyId() {
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.login("", "0"));
    }

    @Test
    public void testLoginFailureEmptyPassword() {
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.login("0", ""));
    }

    @Test
    public void testLoginFailureAlreadyLoggedIn() {
        addWorker();
        workerFacade.login("0", "0");
        assertThrows(IllegalStateException.class, () -> workerFacade.login("0", "0"));
    }

    @Test
    public void testAddNewWorkerSuccess() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        boolean result = addSecondWorker();
        assertTrue(result);
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize + 1, endSize);
    }

    @Test
    public void testAddNewWorkerWithEmptyId() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker("", "Super", "Lee"));
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewWorkerWithBadId() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker("id", "Super", "Lee"));

        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewWorkerWithNullId() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker(null, "Super", "Lee"));
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewWorkerWithDuplicateId() {
        fakeLogin(true);
        addWorker();
        assertThrows(IllegalArgumentException.class, this::addWorker);
    }

    @Test
    public void testAddNewWorkerWithBadFirstname() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker("0", "", "Lee"));
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewWorkerWithBadSurname() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker("0", "Super", ""));
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewWorkerWithNullFirstname() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker("0", null, "Lee"));
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewWorkerWithNullSurname() {
        fakeLogin(true);
        int startSize = workerFacade.getAllWorkers().size();
        assertThrows(IllegalArgumentException.class, () -> workerFacade.addNewWorker("0", "Super", null));
        int endSize = workerFacade.getAllWorkers().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testGetWorkersByNameSuccess() {
        fakeLogin(true);
        addWorker();
        int result = workerFacade.getWorkersByName("Super", "Lee").size();
        assertEquals(1, result);
    }

    @Test
    public void testGetWorkersByNameFailureNonExisting() {
        fakeLogin(true);
        addWorker();
        int result = workerFacade.getWorkersByName("Avi", "Ron").size();
        assertEquals(0, result);
    }

    @Test
    public void testGetWorkersByIdSuccess() {
        fakeLogin(true);
        addWorker();
        WorkerToSend result = workerFacade.getWorkerById("0");
        assertNotNull(result);
    }

    @Test
    public void testGetWorkersByIdFailureNonExisting() {
        fakeLogin(true);
        addWorker();
        assertThrows(NoSuchElementException.class, () -> workerFacade.getWorkerById("1"));
    }

    @Test
    public void testAddRoleSuccess() {
        fakeLogin(true);
        addWorker();
        boolean result = addRoleManager();
        assertTrue(result);
    }

    @Test
    public void testAddRoleFailureDuplicateRole() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        assertThrows(IllegalStateException.class, () -> workerFacade.addWorkerRole("0", "Manager"));
    }

    @Test
    public void testAddRoleFailureNonExistingWorker() {
        fakeLogin(true);
        assertThrows(NoSuchElementException.class, () -> workerFacade.addWorkerRole("0", "Manager"));
    }

    @Test
    public void testAddRoleFailureBadRole() {
        fakeLogin(true);
        addWorker();
        assertThrows(NoSuchElementException.class, () -> workerFacade.addWorkerRole("0", "Emperor"));
    }

    @Test
    public void testGetWorkersByRoleSuccess() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int result = workerFacade.getWorkersByRole("Manager").size();
        assertEquals(1, result);
    }

    @Test
    public void testAssignWorkerSuccess() {
        workerFacade.clearData();
        addWorker();
        addRoleManager();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        boolean result = workerFacade.assignWorker("0", sid, "Manager");
        assertTrue(result);
    }

    @Test
    public void testAssignWorkerFailureNonExistingRole() {
        addWorker();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        assertThrows(IllegalStateException.class, () -> workerFacade.assignWorker("0", sid, "Manager"));
    }

    @Test
    public void testAssignWorkerFailureNotAvailable() {
        addWorker();
        addRoleManager();
        int sid = addShift();
        fakeLogin(true);
        assertThrows(IllegalStateException.class, () -> workerFacade.assignWorker("0", sid, "Manager"));
    }

    @Test
    public void testAssignWorkerFailureAlreadyAssigned() {
        addWorker();
        addRoleManager();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        workerFacade.assignWorker("0", sid, "Manager");
        assertThrows(IllegalStateException.class, () -> workerFacade.assignWorker("0", sid, "Manager"));
    }

    @Test
    public void testUnassignWorkerSuccess() {
        addWorker();
        addRoleManager();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        workerFacade.assignWorker("0", sid, "Manager");
        boolean result = workerFacade.unassignWorker("0", sid);
        assertTrue(result);
    }

    @Test
    public void testUnassignWorkerFailureNonExisting() {
        fakeLogin(true);
        int sid = addShift();
        assertThrows(NoSuchElementException.class, () -> workerFacade.unassignWorker("0", sid));
    }

    @Test
    public void testUnassignWorkerFailureNonAssigned() {
        fakeLogin(true);
        addWorker();
        int sid = addShift();
        assertThrows(IllegalStateException.class, () -> workerFacade.unassignWorker("0", sid));
        fakeLogout();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        assertThrows(IllegalStateException.class, () -> workerFacade.unassignWorker("0", sid));
    }
}
