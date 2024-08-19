import com.Superlee.HR.Backend.Business.BranchFacade;
import com.Superlee.HR.Backend.Business.WorkerFacade;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BranchTests {
    private final BranchFacade branchFacade = BranchFacade.getInstance().setTestMode(true);
    private final WorkerFacade workerFacade = WorkerFacade.getInstance().setTestMode(true);

    @Before
    public void setUp() {
        branchFacade.reset(0xC0FFEE);
        workerFacade.reset(0xC0FFEE);
    }

    @After
    public void cleanUp() {
        workerFacade.clearData();
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

    boolean addBranch() {
        boolean loggedIn = workerFacade.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = branchFacade.addBranch("Hakol BeHinam", "Eliezer Kaplan 1, Jerusalem", "0");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    @Test
    public void testAddNewBranchSuccess() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int startSize = branchFacade.getAllBranches().size();
        boolean result = addBranch();
        assertTrue(result);
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize + 1, endSize);
    }

    @Test
    public void testAddNewBranchWithExistingName() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        addBranch();
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, this::addBranch);
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithNullName() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.addBranch(null, "Eliezer Kaplan 1, Jerusalem", "0"));
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithNullAddress() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.addBranch("Hakol BeHinam", null, "0"));
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithNullManager() {
        fakeLogin(true);
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.addBranch("Hakol BeHinam", "Eliezer Kaplan 1, Jerusalem", null));
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithEmptyName() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.addBranch("", "Eliezer Kaplan 1, Jerusalem", "0"));
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithEmptyAddress() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.addBranch("Hakol BeHinam", "", "0"));
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithEmptyManager() {
        fakeLogin(true);
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.addBranch("Hakol BeHinam", "Eliezer Kaplan 1, Jerusalem", ""));
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithNonExistingManager() {
        fakeLogin(true);
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(NoSuchElementException.class, this::addBranch);
        int endSize = branchFacade.getAllBranches().size();
        assertEquals(startSize, endSize);
    }

    @Test
    public void testAddNewBranchWithNonManager() {
        fakeLogin(true);
        addWorker();
        int startSize = branchFacade.getAllBranches().size();
        assertThrows(IllegalArgumentException.class, this::addBranch);
    }

    @Test
    public void testGetBranchSuccess() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        addBranch();
        assertNotNull(branchFacade.getBranch("Hakol BeHinam"));
    }

    @Test
    public void testGetBranchWithNonExistingName() {
        fakeLogin(true);
        assertThrows(NoSuchElementException.class, () -> branchFacade.getBranch("Hakol BeHinam"));
    }

    @Test
    public void testUpdateManagerSuccess() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        addBranch();
        workerFacade.addNewWorker("1", "Mega", "Lee");
        workerFacade.addWorkerRole("1", "Manager");
        boolean result = branchFacade.updateManager("Hakol BeHinam", "1");
        assertEquals("1", branchFacade.getBranch("Hakol BeHinam").manager());
    }

    @Test
    public void testUpdateManagerWithNonExistingWorker() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        addBranch();
        assertThrows(NoSuchElementException.class, () -> branchFacade.updateManager("Hakol BeHinam", "1"));
    }

    @Test
    public void testUpdateManagerWithNonManager() {
        fakeLogin(true);
        addWorker();
        addRoleManager();
        addBranch();
        workerFacade.addNewWorker("1", "Mega", "Lee");
        assertThrows(IllegalArgumentException.class, () -> branchFacade.updateManager("Hakol BeHinam", "1"));
        assertNotEquals("1", branchFacade.getBranch("Hakol BeHinam").manager());
    }

    @Test
    public void testUpdateManagerWithNonExistingBranch() {
        fakeLogin(true);
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.updateManager("Hakol BeHinam", "0"));
    }

    @Test
    public void testUpdateManagerWithNullBranch() {
        fakeLogin(true);
        addWorker();
        assertThrows(IllegalArgumentException.class, () -> branchFacade.updateManager(null, "0"));
    }
}
