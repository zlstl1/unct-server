package egovframework.upa.restController.unct.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.upa.restController.unct.UnctMapper;
import egovframework.upa.restController.unct.UnctService;

@Service("unctService")
public class UnctServiceImpl implements UnctService{

	@Resource(name="unctMapper")
	private UnctMapper unctMapper;

	@Override
	public String selectVesselStatus() throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectVesselStatus();
	}
	
	@Override
	public String selectAbsAsignList(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectAbsAsignList(map);
	}
	
	@Override
	public String selectVesselLoadList(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectVesselLoadList(map);
	}
	
	@Override
	public String selectCllYard(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectCllYard(map);
	}
	
	@Override
	public String selectNotGateIn(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectNotGateIn(map);
	}
	
	@Override
	public String selectOverGateIn(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectOverGateIn(map);
	}
	
	@Override
	public String selectCancleShipping(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectCancleShipping(map);
	}
	
	@Override
	public String selectGateIO(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectGateIO(map);
	}
	
	@Override
	public String selectGateIOStatus(String truckNo) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectGateIOStatus(truckNo);
	}
	
	@Override
	public String selectContainerInfo(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectContainerInfo(map);
	}
	
	@Override
	public String selectEmptyContainerStatus(String shippingCode) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectEmptyContainerStatus(shippingCode);
	}
	
	@Override
	public String selectBookingList(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectBookingList(map);
	}
	
	@Override
	public String selectCopinoList(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectCopinoList(map);
	}
	
	@Override
	public String selectTerminalShipCode(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectTerminalShipCode(map);
	}
	
	@Override
	public String selectTmlCongestionList() throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectTmlCongestionList();
	}
	
	@Override
	public String selectAnnouncement() throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectAnnouncement();
	}
	
	@Override
	public String selectDangerContainerList(Map<String, String> map) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectDangerContainerList(map);
	}
	
	@Override
	public String selectContainerLoc(String conNoList) throws Exception {
		// TODO Auto-generated method stub
		return unctMapper.selectContainerLoc(conNoList);
	}

}
