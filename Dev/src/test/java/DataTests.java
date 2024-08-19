import com.Superlee.HR.Backend.Business.BranchFacade;
import com.Superlee.HR.Backend.Business.ShiftFacade;
import com.Superlee.HR.Backend.Business.WorkerFacade;
import com.Superlee.HR.Backend.Business.WorkerToSend;
import com.Superlee.HR.Backend.DataAccess.BranchDTO;
import com.Superlee.HR.Backend.DataAccess.ShiftDTO;
import com.Superlee.HR.Backend.DataAccess.WorkerDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DataTests {
    private final BranchFacade bf = BranchFacade.getInstance().setTestMode(true);
    private final ShiftFacade sf = ShiftFacade.getInstance().setTestMode(true);
    private final WorkerFacade wf = WorkerFacade.getInstance().setTestMode(true);
    private List<BranchDTO> mockBranches;
    private Map<String, Integer> mockRoles;
    private List<ShiftDTO> mockShifts;
    private List<WorkerDTO> mockWorkers;

    @Before
    public void setUp() {
        bf.reset(0xC0FFEE);
        sf.reset(0xC0FFEE);
        wf.reset(0xC0FFEE);
    }

    @After
    public void cleanUp() {
        wf.clearData();
    }

    @Test
    public void TestInsertNLoadWorkerSuccess() {
        addWorker();
        wf.reset(0xC0FFEE);
        wf.loadData();
        assertNotNull(wf.login("0", "0"));
    }

    @Test
    public void TestInsertNLoadShiftSuccess() {
        addShift();
        sf.reset(0xC0FFEE);
        sf.loadData();
        assertNotNull(sf.getShift(0));
    }

    @Test
    public void TestInsertNLoadBranchSuccess() {
        addWorker();
        fakeLogin(true);
        wf.addWorkerRole("0", "Manager");
        bf.addBranch("Branch1", "123 Elm Street", "0");
        fakeLogout();
        bf.reset(0xC0FFEE);
        bf.loadData();
        fakeLogin(true);
        assertNotNull(bf.getBranch("Branch1"));
        fakeLogout();
    }

    @Test
    public void TestInsertNLoadRoleSuccess() {
        fakeLogin(true);
        wf.addNewRole("automation");
        fakeLogout();
        wf.reset(0xC0FFEE);
        wf.loadData();
        fakeLogin(true);
        assertTrue(wf.getAllRoles().stream().toList().contains("automation"));
        addWorker();
        wf.addWorkerRole("0", "automation");
        fakeLogout();
        wf.reset(0xC0FFEE);
        wf.loadData();
        fakeLogin(true);
        assertTrue(wf.getWorkerById("0").roles().contains(wf.getAllRoles().size()));
        fakeLogout();
    }

    @Test
    public void TestInsertNLoadMockBranchesSuccess() {
        createMockData();
        insertMockData();
        bf.reset(0xC0FFEE);
        bf.loadData();
        fakeLogin(true);
        mockBranches.forEach(b -> assertNotNull(bf.getBranch(b.getName())));
        fakeLogout();
    }

    @Test
    public void TestInsertNLoadMockWorkersSuccess() {
        createMockData();
        insertMockData();
        wf.reset(0xC0FFEE);
        wf.loadData();
        fakeLogin(true);

        mockWorkers.forEach(w -> {
            assertNotNull(wf.getWorkerById(w.getId()));
            assertTrue(wf.getWorkerById(w.getId()).roles().containsAll(w.getRoles()));
            assertTrue(w.getRoles().containsAll(wf.getWorkerById(w.getId()).roles()));
            assertEquals(w.getShifts(), wf.getWorkerShifts(w.getId()));
            assertTrue(wf.getWorkerAvailability(w.getId()).containsAll(w.getAvailability()));
        });
        fakeLogout();
    }

    @Test
    public void TestInsertNLoadMockShiftsSuccess() {
        createMockData();
        insertMockData();
        sf.reset(0xC0FFEE);
        sf.loadData();
        fakeLogin(true);
        mockShifts.forEach(s -> {
            int id = s.getId();
            assertNotNull(sf.getShift(id));
            assertEquals(LocalDateTime.parse(sf.getShift(id).startTime()), s.getStartTime());
            assertEquals(LocalDateTime.parse(sf.getShift(id).endTime()), s.getEndTime());
            assertEquals(sf.getShiftRequiredWorkersOfRole(id), s.getRequiredRoles());

            assertTrue(sf.getAssignableWorkersForShift(id).stream().map(WorkerToSend::id).toList().containsAll(s.getAvailableWorkers()));
            assertTrue(s.getAvailableWorkers().containsAll(sf.getAssignableWorkersForShift(id).stream().map(WorkerToSend::id).toList()));

            assertTrue(sf.getWorkersByShift(id).stream().map(WorkerToSend::id).toList().containsAll(s.getAssignedWorkers()));
            assertTrue(s.getAssignedWorkers().containsAll(sf.getWorkersByShift(id).stream().map(WorkerToSend::id).toList()));
            assertEquals(sf.getWorkerRolesByShift(id), s.getWorkerRoles());
        });
        fakeLogout();
    }

    @Test
    public void TestUpdateWorkerSuccess() {
        addWorker();
        wf.reset(0xC0FFEE);
        wf.loadData();
        wf.login("0", "0");

        wf.updateWorkerEmail("0", "mr.poopybutthole@company.com");
        wf.updateWorkerPhone("0", "555-1234");
        wf.updateWorkerPassword("0", "password1");
        wf.updateWorkerBankDetails("0", "123456789");
        wf.updateWorkerContractDetails("0", "permanent");
        wf.logout("0");

        fakeLogin(true);
        assertTrue(wf.updateWorkerSalary("0", 100000));
        fakeLogout();
        wf.reset(0xC0FFEE);
        wf.loadData();
        fakeLogin(true);
        assertEquals("mr.poopybutthole@company.com", wf.getWorkerById("0").email());
        assertEquals("555-1234", wf.getWorkerById("0").phone());
        assertNotNull(wf.login("0", "password1"));

        assertEquals("123456789", wf.getWorkerBankDetails("0"));
        assertEquals("permanent", wf.getWorkerById("0").contract());
        assertEquals(100000, wf.getWorkerById("0").salary());
        fakeLogout();
    }

    @Test
    public void TestUpdateAssignSuccess() {
        addWorker();
        addRoleManager();
        addSecondWorker();
        addShift();
        fakeLogin(true);
        wf.addWorkerRole("1", "Cashier");
        fakeLogout();

        wf.login("0", "0");
        sf.addAvailability("0", 0);
        wf.login("1", "1");
        sf.addAvailability("1", 0);
        fakeLogin(true);
        sf.assignWorker("0", 0, "Manager");
        sf.assignWorker("1", 0, "Cashier");
        fakeLogout();
        sf.reset(0xC0FFEE);
        sf.loadData();
        fakeLogin(true);
        assertTrue(sf.getWorkersByShift(0).stream().map(WorkerToSend::id).toList().contains("0"));
        assertTrue(sf.getWorkersByShift(0).stream().map(WorkerToSend::id).toList().contains("1"));
        assertEquals(1, (int) sf.getWorkerRolesByShift(0).get("0"));
        assertEquals(2, (int) sf.getWorkerRolesByShift(0).get("1"));
        fakeLogout();
    }

    @Test
    public void TestUpdateUnassignSuccess() {
        addWorker();
        addRoleManager();
        addSecondWorker();
        addShift();
        fakeLogin(true);
        wf.addWorkerRole("1", "Cashier");
        fakeLogout();

        wf.login("0", "0");
        sf.addAvailability("0", 0);
        wf.login("1", "1");
        sf.addAvailability("1", 0);
        fakeLogin(true);
        sf.assignWorker("0", 0, "Manager");
        sf.assignWorker("1", 0, "Cashier");
        fakeLogout();
        sf.reset(0xC0FFEE);
        sf.loadData();
        fakeLogin(true);
        assertTrue(sf.getWorkersByShift(0).stream().map(WorkerToSend::id).toList().contains("0"));
        assertTrue(sf.getWorkersByShift(0).stream().map(WorkerToSend::id).toList().contains("1"));
        assertEquals(1, (int) sf.getWorkerRolesByShift(0).get("0"));
        assertEquals(2, (int) sf.getWorkerRolesByShift(0).get("1"));

        sf.unassignWorker("0", 0);
        sf.unassignWorker("1", 0);
        fakeLogout();
        sf.reset(0xC0FFEE);
        sf.loadData();

        fakeLogin(true);
        assertFalse(sf.getWorkersByShift(0).stream().map(WorkerToSend::id).toList().contains("0"));
        assertFalse(sf.getWorkersByShift(0).stream().map(WorkerToSend::id).toList().contains("1"));
        assertNull(sf.getWorkerRolesByShift(0).get("0"));
        assertNull(sf.getWorkerRolesByShift(0).get("1"));
        fakeLogout();
    }

    // =================================================================================
    // Helper methods
    // =================================================================================
    private int addShift() {
        boolean loggedIn = wf.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        int result = sf.addNewShift("2025-01-01T08:00", "2025-01-01T16:00", "Hakol BeHinam");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private boolean addWorker() {
        boolean loggedIn = wf.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = wf.addNewWorker("0", "Super", "Lee");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private boolean addRoleManager() {
        boolean loggedIn = wf.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = wf.addWorkerRole("0", "Manager");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private void login() {
        wf.login("0", "0");
    }

    private boolean addSecondWorker() {
        boolean loggedIn = wf.isLoggedInHRManager();
        if (!loggedIn)
            fakeLogin(true);
        boolean result = wf.addNewWorker("1", "Avi", "Ron");
        if (!loggedIn)
            fakeLogout();
        return result;
    }

    private void fakeLogin(boolean hrm) {
        wf.fakeLogin(hrm, "000");
    }

    private void fakeLogout() {
        wf.fakeLogout();
    }

    public void createMockData() {

        mockRoles = Map.of(
                "HRManager", 0,
                "Manager", 1,
                "Cashier", 2,
                "Cleaner", 3,
                "Storekeeper", 4,
                "Security", 5,
                "Driver", 6
        );

        mockWorkers = Arrays.asList(
                new WorkerDTO("0", "Mr", "Poopybutthole", "mr.poopybutthole@company.com", "555-1234", "123", "123456789", 100000, List.of(0), new ArrayList<>(), Arrays.asList(1, 2, 3), LocalDateTime.now(), "permanent", "Head Office"),
                new WorkerDTO("1", "Homer", "Simpson", "homer.simpson@company.com", "555-2345", "password", "234567890", 50000, Arrays.asList(1, 2), List.of(1), Arrays.asList(1, 2), LocalDateTime.now(), "contract", "Branch1"),
                new WorkerDTO("2", "Peter", "Griffin", "peter.griffin@company.com", "555-3456", "password", "345678901", 48000, Arrays.asList(2, 3), Arrays.asList(1, 2), Arrays.asList(1, 2, 3), LocalDateTime.now(), "contract", "Branch1"),
                new WorkerDTO("3", "SpongeBob", "SquarePants", "spongebob.squarepants@company.com", "555-4567", "password", "456789012", 52000, Arrays.asList(3, 4), List.of(2), Arrays.asList(1, 2, 3), LocalDateTime.now(), "contract", "Branch2"),
                new WorkerDTO("4", "Daffy", "Duck", "daffy.duck@company.com", "555-5678", "password", "567890123", 49000, Arrays.asList(4, 5), List.of(2), Arrays.asList(1, 2), LocalDateTime.now(), "contract", "Branch2"),
                new WorkerDTO("5", "Bugs", "Bunny", "bugs.bunny@company.com", "555-6789", "password", "678901234", 51000, Arrays.asList(5, 6), List.of(3), Arrays.asList(2, 3), LocalDateTime.now(), "contract", "Branch3"),
                new WorkerDTO("6", "Shrek", "3D", "shrek@company.com", "555-7890", "password", "789012345", 47000, Arrays.asList(0, 1), List.of(3), Arrays.asList(2, 3), LocalDateTime.now(), "contract", "Branch3"),
                new WorkerDTO("7", "Charlie", "Brown", "charlie.brown@company.com", "555-8901", "password", "890123456", 53000, Arrays.asList(2, 3), List.of(1), Arrays.asList(1, 2), LocalDateTime.now(), "contract", "Branch1"),
                new WorkerDTO("8", "Scooby", "Doo", "scooby.doo@company.com", "555-9012", "password", "901234567", 48000, Arrays.asList(3, 4), List.of(2), Arrays.asList(2, 3), LocalDateTime.now(), "contract", "Branch2"),
                new WorkerDTO("9", "Unity", "Noob", "unity@company.com", "555-0123", "password", "012345678", 49000, Arrays.asList(4, 5), List.of(3), Arrays.asList(2, 3), LocalDateTime.now(), "contract", "Branch3"),
                new WorkerDTO("10", "Noob", "Noob", "noob.noob@company.com", "555-1230", "password", "123456789", 50000, Arrays.asList(5, 6), List.of(4), Arrays.asList(1, 3), LocalDateTime.now(), "contract", "Branch3")
        );


        mockBranches = Arrays.asList(
                new BranchDTO("Branch1", "123 Elm Street", "Manager1"),
                new BranchDTO("Branch2", "456 Oak Avenue", "Manager2"),
                new BranchDTO("Branch3", "789 Pine Road", "Manager3")
        );

        mockShifts = Arrays.asList(
                new ShiftDTO(1, "Branch1", LocalDateTime.of(2024, 7, 1, 8, 0, 0), LocalDateTime.of(2024, 7, 1, 12, 0, 0), Map.of("Manager", 1, "Cashier", 2), Arrays.asList("1", "2", "7"), Arrays.asList("1", "2"), Map.of("1", 1, "2", 2)),
                new ShiftDTO(2, "Branch2", LocalDateTime.of(2024, 7, 1, 12, 0, 0), LocalDateTime.of(2024, 7, 1, 16, 0, 0), Map.of("Manager", 1, "Cashier", 1, "Cleaner", 3), Arrays.asList("3", "4", "8"), Arrays.asList("3", "4", "8"), Map.of("3", 1, "4", 2, "8", 3)),
                new ShiftDTO(3, "Branch1", LocalDateTime.of(2024, 7, 1, 16, 0, 0), LocalDateTime.of(2024, 7, 1, 20, 0, 0), Map.of("Manager", 1, "Storekeeper", 1), Arrays.asList("5", "6", "9"), Arrays.asList("5", "6"), Map.of("5", 1, "6", 4)),
                new ShiftDTO(4, "Branch3", LocalDateTime.of(2024, 7, 2, 8, 0, 0), LocalDateTime.of(2024, 7, 2, 12, 0, 0), Map.of("Manager", 1, "Cashier", 1), Arrays.asList("10", "1", "2"), Arrays.asList("10", "1"), Map.of("10", 1, "1", 2))
        );
    }

    public void insertMockData() {
        mockBranches.forEach(branchDTO -> {
            bf.setDTO(branchDTO);
            bf.getDTO().insert();
        });
        //mockRoles.forEach(wf::addNewRole);
        mockWorkers.forEach(workerDTO -> {
            wf.setDTO(workerDTO);
            wf.getDTO().insert();
        });
        //mockWorkers.forEach(w -> w. roles().forEach(r -> wf.addWorkerRole(w.id(), r)));
        mockShifts.forEach(shiftDTO -> {
            sf.setDTO(shiftDTO);
            sf.getDTO().insert();
        });
    }
}
