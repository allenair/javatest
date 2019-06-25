package allen.tio;

import java.util.ArrayList;
import java.util.List;

import allen.tio.client.AllenClient;

public class TioTestApp {
	public static List<String> mockClientIdList = new ArrayList<>();
	static {
		mockClientIdList.add("mock_client_00");
		mockClientIdList.add("mock_client_01");
		mockClientIdList.add("mock_client_02");
		mockClientIdList.add("mock_client_03");
		mockClientIdList.add("mock_client_04");
		mockClientIdList.add("mock_client_05");
		mockClientIdList.add("mock_client_06");
		mockClientIdList.add("mock_client_07");
		mockClientIdList.add("mock_client_08");
		mockClientIdList.add("mock_client_09");
		mockClientIdList.add("mock_client_10");
		mockClientIdList.add("mock_client_11");
		mockClientIdList.add("mock_client_12");
		mockClientIdList.add("mock_client_13");
		mockClientIdList.add("mock_client_14");
		mockClientIdList.add("mock_client_15");
		mockClientIdList.add("mock_client_16");
		mockClientIdList.add("mock_client_17");
		mockClientIdList.add("mock_client_18");
		mockClientIdList.add("mock_client_19");
	}
	
	public static void main(String[] args) throws Exception {
		AllenClient.send();
	}

}
