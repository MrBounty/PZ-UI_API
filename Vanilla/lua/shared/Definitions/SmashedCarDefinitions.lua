--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 26/05/2021
-- Time: 11:59
-- To change this template use File | Settings | File Templates.
--

SmashedCarDefinitions = SmashedCarDefinitions or {};

-- define all variant of a base car with his smashed version, if we don't find a value when we try to make a car smashed, we'll just use the base version of the car
SmashedCarDefinitions.cars = {};
SmashedCarDefinitions.cars["Base.CarNormal"] = {};
SmashedCarDefinitions.cars["Base.CarNormal"].front = "Base.CarNormalSmashedFront";
SmashedCarDefinitions.cars["Base.CarNormal"].rear = "Base.CarNormalSmashedRear";
SmashedCarDefinitions.cars["Base.CarNormal"].left = "Base.CarNormalSmashedLeft";
SmashedCarDefinitions.cars["Base.CarNormal"].right = "Base.CarNormalSmashedRight";

SmashedCarDefinitions.cars["Base.PickUpTruck"] = {};
SmashedCarDefinitions.cars["Base.PickUpTruck"].rear = "Base.PickUpTruckSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpTruck"].front = "Base.PickUpTruckSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpTruck"].right = "Base.PickUpTruckSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpTruck"].left = "Base.PickUpTruckSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpTruck"].skin = 0;

SmashedCarDefinitions.cars["Base.PickUpTruckMccoy"] = {};
SmashedCarDefinitions.cars["Base.PickUpTruckMccoy"].rear = "Base.PickUpTruckSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpTruckMccoy"].front = "Base.PickUpTruckSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpTruckMccoy"].right = "Base.PickUpTruckSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpTruckMccoy"].left = "Base.PickUpTruckSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpTruckMccoy"].skin = 1;

SmashedCarDefinitions.cars["Base.ModernCar"] = {};
SmashedCarDefinitions.cars["Base.ModernCar"].rear = "Base.ModernCarSmashedRear";
SmashedCarDefinitions.cars["Base.ModernCar"].front = "Base.ModernCarSmashedFront";
SmashedCarDefinitions.cars["Base.ModernCar"].right = "Base.ModernCarSmashedRight";
SmashedCarDefinitions.cars["Base.ModernCar"].left = "Base.ModernCarSmashedLeft";

SmashedCarDefinitions.cars["Base.ModernCar02"] = {};
SmashedCarDefinitions.cars["Base.ModernCar02"].rear = "Base.ModernCar02SmashedRear";
SmashedCarDefinitions.cars["Base.ModernCar02"].front = "Base.ModernCar02SmashedFront";
SmashedCarDefinitions.cars["Base.ModernCar02"].right = "Base.ModernCar02SmashedRight";
SmashedCarDefinitions.cars["Base.ModernCar02"].left = "Base.ModernCar02SmashedLeft";

SmashedCarDefinitions.cars["Base.CarLights"] = {};
SmashedCarDefinitions.cars["Base.CarLights"].rear = "Base.CarLightsSmashedRear";
SmashedCarDefinitions.cars["Base.CarLights"].front = "Base.CarLightsSmashedFront";
SmashedCarDefinitions.cars["Base.CarLights"].right = "Base.CarLightsSmashedRight";
SmashedCarDefinitions.cars["Base.CarLights"].left = "Base.CarLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.CarLights"].skin = 0;

SmashedCarDefinitions.cars["Base.CarLightsPolice"] = {};
SmashedCarDefinitions.cars["Base.CarLightsPolice"].rear = "Base.CarLightsSmashedRear";
SmashedCarDefinitions.cars["Base.CarLightsPolice"].front = "Base.CarLightsSmashedFront";
SmashedCarDefinitions.cars["Base.CarLightsPolice"].right = "Base.CarLightsSmashedRight";
SmashedCarDefinitions.cars["Base.CarLightsPolice"].left = "Base.CarLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.CarLightsPolice"].skin = 1;

SmashedCarDefinitions.cars["Base.CarStationWagon"] = {};
SmashedCarDefinitions.cars["Base.CarStationWagon"].rear = "Base.CarStationWagonSmashedRear";
SmashedCarDefinitions.cars["Base.CarStationWagon"].front = "Base.CarStationWagonSmashedFront";
SmashedCarDefinitions.cars["Base.CarStationWagon"].right = "Base.CarStationWagonSmashedRight";
SmashedCarDefinitions.cars["Base.CarStationWagon"].left = "Base.CarStationWagonSmashedLeft";
SmashedCarDefinitions.cars["Base.CarStationWagon"].skin = 0;

SmashedCarDefinitions.cars["Base.CarStationWagon2"] = {};
SmashedCarDefinitions.cars["Base.CarStationWagon2"].rear = "Base.CarStationWagonSmashedRear";
SmashedCarDefinitions.cars["Base.CarStationWagon2"].front = "Base.CarStationWagonSmashedFront";
SmashedCarDefinitions.cars["Base.CarStationWagon2"].right = "Base.CarStationWagonSmashedRight";
SmashedCarDefinitions.cars["Base.CarStationWagon2"].left = "Base.CarStationWagonSmashedLeft";
SmashedCarDefinitions.cars["Base.CarStationWagon2"].skin = 1;

SmashedCarDefinitions.cars["Base.CarLuxury"] = {};
SmashedCarDefinitions.cars["Base.CarLuxury"].rear = "Base.CarLuxurySmashedRear";
SmashedCarDefinitions.cars["Base.CarLuxury"].front = "Base.CarLuxurySmashedFront";
SmashedCarDefinitions.cars["Base.CarLuxury"].right = "Base.CarLuxurySmashedRight";
SmashedCarDefinitions.cars["Base.CarLuxury"].left = "Base.CarLuxurySmashedLeft";

SmashedCarDefinitions.cars["Base.OffRoad"] = {};
SmashedCarDefinitions.cars["Base.OffRoad"].rear = "Base.OffRoadSmashedRear";
SmashedCarDefinitions.cars["Base.OffRoad"].front = "Base.OffRoadSmashedFront";
SmashedCarDefinitions.cars["Base.OffRoad"].right = "Base.OffRoadSmashedRight";
SmashedCarDefinitions.cars["Base.OffRoad"].left = "Base.OffRoadSmashedLeft";

SmashedCarDefinitions.cars["Base.PickUpTruckLights"] = {};
SmashedCarDefinitions.cars["Base.PickUpTruckLights"].rear = "Base.PickUpTruckLightsSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpTruckLights"].front = "Base.PickUpTruckLightsSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpTruckLights"].right = "Base.PickUpTruckLightsSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpTruckLights"].left = "Base.PickUpTruckLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpTruckLights"].skin = -1;

SmashedCarDefinitions.cars["Base.PickUpTruckLightsFire"] = {};
SmashedCarDefinitions.cars["Base.PickUpTruckLightsFire"].rear = "Base.PickUpTruckLightsSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpTruckLightsFire"].front = "Base.PickUpTruckLightsSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpTruckLightsFire"].right = "Base.PickUpTruckLightsSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpTruckLightsFire"].left = "Base.PickUpTruckLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpTruckLightsFire"].skin = 2;

SmashedCarDefinitions.cars["Base.PickUpVan"] = {};
SmashedCarDefinitions.cars["Base.PickUpVan"].rear = "Base.PickUpVanSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpVan"].front = "Base.PickUpVanSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpVan"].right = "Base.PickUpVanSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpVan"].left = "Base.PickUpVanSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpVan"].skin = 0;

SmashedCarDefinitions.cars["Base.PickUpVanMccoy"] = {};
SmashedCarDefinitions.cars["Base.PickUpVanMccoy"].rear = "Base.PickUpVanSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpVanMccoy"].front = "Base.PickUpVanSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpVanMccoy"].right = "Base.PickUpVanSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpVanMccoy"].left = "Base.PickUpVanSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpVanMccoy"].skin = 1;

SmashedCarDefinitions.cars["Base.PickUpVanLights"] = {};
SmashedCarDefinitions.cars["Base.PickUpVanLights"].rear = "Base.PickUpVanLightsSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].front = "Base.PickUpVanLightsSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].right = "Base.PickUpVanLightsSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].left = "Base.PickUpVanLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].skin = -1;

SmashedCarDefinitions.cars["Base.PickUpVanLights"] = {};
SmashedCarDefinitions.cars["Base.PickUpVanLights"].rear = "Base.PickUpVanLightsSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].front = "Base.PickUpVanLightsSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].right = "Base.PickUpVanLightsSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].left = "Base.PickUpVanLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpVanLights"].skin = -1;

SmashedCarDefinitions.cars["Base.PickUpVanLightsFire"] = {};
SmashedCarDefinitions.cars["Base.PickUpVanLightsFire"].rear = "Base.PickUpVanLightsSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpVanLightsFire"].front = "Base.PickUpVanLightsSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpVanLightsFire"].right = "Base.PickUpVanLightsSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpVanLightsFire"].left = "Base.PickUpVanLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpVanLightsFire"].skin = 2;

SmashedCarDefinitions.cars["Base.PickUpVanLightsPolice"] = {};
SmashedCarDefinitions.cars["Base.PickUpVanLightsPolice"].rear = "Base.PickUpVanLightsSmashedRear";
SmashedCarDefinitions.cars["Base.PickUpVanLightsPolice"].front = "Base.PickUpVanLightsSmashedFront";
SmashedCarDefinitions.cars["Base.PickUpVanLightsPolice"].right = "Base.PickUpVanLightsSmashedRight";
SmashedCarDefinitions.cars["Base.PickUpVanLightsPolice"].left = "Base.PickUpVanLightsSmashedLeft";
SmashedCarDefinitions.cars["Base.PickUpVanLightsPolice"].skin = 3;

SmashedCarDefinitions.cars["Base.SmallCar"] = {};
SmashedCarDefinitions.cars["Base.SmallCar"].rear = "Base.CarSmallSmashedRear";
SmashedCarDefinitions.cars["Base.SmallCar"].front = "Base.CarSmallSmashedFront";
SmashedCarDefinitions.cars["Base.SmallCar"].right = "Base.CarSmallSmashedRight";
SmashedCarDefinitions.cars["Base.SmallCar"].left = "Base.CarSmallSmashedLeft";

SmashedCarDefinitions.cars["Base.SmallCar02"] = {};
SmashedCarDefinitions.cars["Base.SmallCar02"].rear = "Base.CarSmall02SmashedRear";
SmashedCarDefinitions.cars["Base.SmallCar02"].front = "Base.CarSmall02SmashedFront";
SmashedCarDefinitions.cars["Base.SmallCar02"].right = "Base.CarSmall02SmashedRight";
SmashedCarDefinitions.cars["Base.SmallCar02"].left = "Base.CarSmall02SmashedLeft";

SmashedCarDefinitions.cars["Base.StepVan"] = {};
SmashedCarDefinitions.cars["Base.StepVan"].rear = "Base.StepVanSmashedRear";
SmashedCarDefinitions.cars["Base.StepVan"].front = "Base.StepVanSmashedFront";
SmashedCarDefinitions.cars["Base.StepVan"].right = "Base.StepVanSmashedRight";
SmashedCarDefinitions.cars["Base.StepVan"].left = "Base.StepVanSmashedLeft";

SmashedCarDefinitions.cars["Base.StepVanMail"] = {};
SmashedCarDefinitions.cars["Base.StepVanMail"].rear = "Base.StepVanMailSmashedRear";
SmashedCarDefinitions.cars["Base.StepVanMail"].front = "Base.StepVanMailSmashedFront";
SmashedCarDefinitions.cars["Base.StepVanMail"].right = "Base.StepVanMailSmashedRight";
SmashedCarDefinitions.cars["Base.StepVanMail"].left = "Base.StepVanMailSmashedLeft";

SmashedCarDefinitions.cars["Base.SUV"] = {};
SmashedCarDefinitions.cars["Base.SUV"].rear = "Base.SUVSmashedRear";
SmashedCarDefinitions.cars["Base.SUV"].front = "Base.SUVSmashedFront";
SmashedCarDefinitions.cars["Base.SUV"].right = "Base.SUVSmashedRight";
SmashedCarDefinitions.cars["Base.SUV"].left = "Base.SUVSmashedLeft";

--SmashedCarDefinitions.cars["Base.SmallCar02"] = {};
--SmashedCarDefinitions.cars["Base.SmallCar02"].rear = "Base.CarSmallSmashedRear";
--SmashedCarDefinitions.cars["Base.SmallCar02"].front = "Base.CarSmallSmashedFront";
--SmashedCarDefinitions.cars["Base.SmallCar02"].right = "Base.CarSmallSmashedRight";
--SmashedCarDefinitions.cars["Base.SmallCar02"].left = "Base.CarSmallSmashedLeft";
--SmashedCarDefinitions.cars["Base.SmallCar02"].skin = 1;