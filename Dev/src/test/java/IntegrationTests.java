import com.Superlee.HR.Backend.Business.WorkerFacade;
import com.Superlee.HR.Backend.Service.HRService;
import com.Superlee.HR.Frontend.ModelFactory;
import com.Superlee.HR.Frontend.WorkerModel;
import com.Superlee.Supply.Business.SupplierFacade;
import com.Superlee.Supply.Service.Responses.Response;
import com.Superlee.Supply.Service.SupplierService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

public class IntegrationTests {
    @Before
    public void setUp() {
        WorkerFacade.getInstance().setTestMode(false);
    }

    @Test
    public void testSupplyManagerCanUseSupplySystem() {
        HRService hrService = HRService.getInstance();
        hrService.loadData();
        hrService.login("0", "123");
        String res = hrService.getWorkersByRole("SupplyManager");
        hrService.logout("0");
        List<WorkerModel> workers = ModelFactory.createWorkerModelList(res);
        assertFalse(workers.isEmpty());

        SupplierService supplierService = new SupplierService(new SupplierFacade());
        Response response = supplierService.loadData();
        assertFalse(response.errorOccurred());
    }
}
