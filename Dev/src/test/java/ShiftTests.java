import com.Superlee.HR.Backend.Business.ShiftFacade;
import com.Superlee.HR.Backend.Business.ShiftToSend;
import com.Superlee.HR.Backend.Business.WorkerFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ShiftTests {
    private final WorkerFacade workerFacade = WorkerFacade.getInstance().setTestMode(true);
    private final ShiftFacade shiftFacade = ShiftFacade.getInstance().setTestMode(true);
    private final String branch = "Hakol BeHinam";

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
        int result = shiftFacade.addNewShift("2025-01-01T08:00", "2025-01-01T16:00", branch);
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

    private void fakeLogin(boolean hrm) {
        workerFacade.fakeLogin(hrm, "000");
    }

    private void fakeLogout() {
        workerFacade.fakeLogout();
    }

    @Test
    public void testAddNewShiftSuccess() {
        fakeLogin(true);
        int startSize = shiftFacade.getAllShifts().size();
        int result = addShift();
        assertNotEquals(-1, result);
        int endSize = shiftFacade.getAllShifts().size();
        assertEquals(startSize + 1, endSize);
    }

    @Test
    public void testAddNewShiftWithNullStart() {
        fakeLogin(true);
        int startSize = shiftFacade.getAllShifts().size();
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.addNewShift(branch, null, "2025-01-01T16:00"));
        int endSize = shiftFacade.getAllShifts().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewShiftWithNullEnd() {
        fakeLogin(true);
        int startSize = shiftFacade.getAllShifts().size();
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.addNewShift(branch, "2025-01-01T08:00", null));
        int endSize = shiftFacade.getAllShifts().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewShiftWithStartEqualToEnd() {
        fakeLogin(true);
        int startSize = shiftFacade.getAllShifts().size();
        assertThrows(DateTimeException.class, () -> shiftFacade.addNewShift(branch, "2025-01-01T08:00", "2025-01-01T08:00"));
        int endSize = shiftFacade.getAllShifts().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewShiftWithEndBeforeStart() {
        fakeLogin(true);
        int startSize = shiftFacade.getAllShifts().size();
        assertThrows(DateTimeException.class, () -> shiftFacade.addNewShift(branch, "2025-01-01T16:00", "2025-01-01T08:00"));
        int endSize = shiftFacade.getAllShifts().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testGetShiftSuccess() {
        int sid = addShift();
        ShiftToSend result = shiftFacade.getShift(sid);
        assertNotNull(result);
    }

    @Test
    public void testGetShiftWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.getShift(-1));
    }

    @Test
    public void testGetShiftWithNonExistingId() {
        assertThrows(NoSuchElementException.class, () -> shiftFacade.getShift(0));
    }

    @Test
    public void testAssignWorkerSuccess() {
        addWorker();
        addRoleManager();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        boolean result = shiftFacade.assignWorker("0", sid, "Manager");
        assertTrue(result);
    }

    @Test
    public void testAssignWorkerFailureNotAvailable() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int sid = addShift();
        assertThrows(IllegalStateException.class, () -> shiftFacade.assignWorker("0", sid, "Manager"));
    }

    @Test
    public void testAssignWorkerFailureNonExistingShift() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        login();
        assertThrows(NoSuchElementException.class, () -> shiftFacade.addAvailability("0", 0));
        fakeLogout();
        fakeLogin(true);
        assertThrows(NoSuchElementException.class, () -> shiftFacade.assignWorker("0", 0, "Manager"));
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.assignWorker("0", -1, "Manager"));
    }

    @Test
    public void testAssignWorkerFailureNonExistingRole() {
        addWorker();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        assertThrows(IllegalStateException.class, () -> shiftFacade.assignWorker("0", sid, "Manager"));
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
        shiftFacade.assignWorker("0", sid, "Manager");
        assertThrows(IllegalStateException.class, () -> shiftFacade.assignWorker("0", sid, "Manager"));
    }

    @Test
    public void testAssignDriverSuccess() {
        addWorker();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        workerFacade.addWorkerRole("0", "Driver");
        workerFacade.addNewWorker("1", "hello", "world");
        workerFacade.addWorkerRole("1", "Storekeeper");
        fakeLogout();
        workerFacade.login("1", "1");
        shiftFacade.addAvailability("1", sid);
        fakeLogout();
        fakeLogin(true);
        shiftFacade.assignWorker("1", sid, "Storekeeper");
        boolean result = shiftFacade.assignWorker("0", sid, "Driver");
        assertTrue(result);
    }

    @Test
    public void testAssignDriverFailureNoStorekeeper() {
        addWorker();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        workerFacade.addWorkerRole("0", "Driver");
        fakeLogin(true);
        assertThrows(IllegalStateException.class, () -> shiftFacade.assignWorker("0", sid, "Driver"));
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
        shiftFacade.assignWorker("0", sid, "Manager");
        boolean result = shiftFacade.unassignWorker("0", sid);
        assertTrue(result);
    }

    @Test
    public void testUnassignWorkerFailureNonExistingShift() {
        fakeLogin(true);
        addWorker();
        assertThrows(NoSuchElementException.class, () -> shiftFacade.unassignWorker("0", 0));
    }

    @Test
    public void testUnassignWorkerFailureNonAssigned() {
        fakeLogin(true);
        addWorker();
        int sid = addShift();
        assertThrows(IllegalStateException.class, () -> shiftFacade.unassignWorker("0", sid));
    }

    @Test
    public void testGetWorkersByShiftSuccessEmptyShift() {
        fakeLogin(true);
        int sid = addShift();
        int result = shiftFacade.getWorkersByShift(sid).size();
        assertEquals(0, result);
    }

    @Test
    public void testGetWorkersByShiftSuccess() {
        addWorker();
        addRoleManager();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        shiftFacade.assignWorker("0", sid, "Manager");
        int result = shiftFacade.getWorkersByShift(sid).size();
        assertEquals(1, result);
    }

    @Test
    public void testGetWorkersByShiftWithInvalidId() {
        fakeLogin(true);
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.getWorkersByShift(-1));
    }

    @Test
    public void testGetWorkersByShiftWithNonExistingId() {
        fakeLogin(true);
        assertThrows(NoSuchElementException.class, () -> shiftFacade.getWorkersByShift(0));

    }

    @Test
    public void testGetAssignableWorkersForShiftSuccessEmptyShift() {
        fakeLogin(true);
        int sid = addShift();
        int result = shiftFacade.getAssignableWorkersForShift(sid).size();
        assertEquals(0, result);
    }

    @Test
    public void testGetAssignableWorkersForShiftSuccess() {
        addWorker();
        addRoleManager();
        int sid = addShift();
        login();
        shiftFacade.addAvailability("0", sid);
        fakeLogout();
        fakeLogin(true);
        shiftFacade.assignWorker("0", sid, "Manager");
        int result = shiftFacade.getAssignableWorkersForShift(sid).size();
        assertEquals(1, result);
    }

    @Test
    public void testGetAssignableWorkersForShiftWithInvalidId() {
        fakeLogin(true);
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.getAssignableWorkersForShift(-1));
    }

    @Test
    public void testGetAssignableWorkersForShiftWithNonExistingId() {
        fakeLogin(true);
        assertThrows(NoSuchElementException.class, () -> shiftFacade.getAssignableWorkersForShift(0));
    }

    @Test
    public void testSetShiftRequiredWorkersOfRoleSuccess() {
        fakeLogin(true);
        int sid = addShift();
        boolean result = shiftFacade.setShiftRequiredWorkersOfRole(sid, "Manager", 1);
        assertTrue(result);
    }

    @Test
    public void testSetShiftRequiredWorkersOfRoleFailureNonExistingShift() {
        fakeLogin(true);
        assertThrows(NoSuchElementException.class, () -> shiftFacade.setShiftRequiredWorkersOfRole(0, "Manager", 1));
    }

    @Test
    public void testSetShiftRequiredWorkersOfRoleFailureNonExistingRole() {
        fakeLogin(true);
        int sid = addShift();
        assertThrows(NoSuchElementException.class, () -> shiftFacade.setShiftRequiredWorkersOfRole(sid, "Emperor", 1));
    }

    @Test
    public void testSetShiftRequiredWorkersOfRoleFailureNegativeAmount() {
        fakeLogin(true);
        int sid = addShift();
        assertThrows(IllegalArgumentException.class, () -> shiftFacade.setShiftRequiredWorkersOfRole(sid, "Manager", -1));
    }
}