package egovframework.upa.restController.unct;

import java.util.List;
import java.util.Map;

public interface UnctService {
	String selectVesselStatus() throws Exception;
	String selectAbsAsignList(Map<String, String> map) throws Exception;
	String selectVesselLoadList(Map<String, String> map) throws Exception;
	String selectCllYard(Map<String, String> map) throws Exception;
	String selectNotGateIn(Map<String, String> map) throws Exception;
	String selectOverGateIn(Map<String, String> map) throws Exception;
	String selectCancleShipping(Map<String, String> map) throws Exception;
	String selectGateIO(Map<String, String> map) throws Exception;
	String selectGateIOStatus(String truckNo) throws Exception;
	String selectContainerInfo(Map<String, String> map) throws Exception;
	String selectEmptyContainerStatus(String shippingCode) throws Exception;
	String selectBookingList(Map<String, String> map) throws Exception;
	String selectCopinoList(Map<String, String> map) throws Exception;
	String selectTerminalShipCode(Map<String, String> map) throws Exception;
	String selectTmlCongestionList() throws Exception;
	String selectAnnouncement() throws Exception;
	String selectDangerContainerList(Map<String, String> map) throws Exception;
	String selectContainerLoc(String conNoList) throws Exception;
}
