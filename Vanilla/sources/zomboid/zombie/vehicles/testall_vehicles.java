package zombie.vehicles;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({test_VehicleInterpolation.class, test_IsQuadranglesAreTransposed.class, test_VehicleCache.class})
public class testall_vehicles {
}
