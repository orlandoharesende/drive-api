package br.jus.tjes.integracao.drive.controller;

public class Configs {

	private static final String CORRELATION_ID = "w_g:831a1d28-412c-4b9e-b65f-476d23db4e13";
	private static final String BASE_URL = "https://prod-eb5ad7951f03966.wdckeystone.com";
	private static final String ACCESS_TOKEN = "eyJraWQiOiJNOHNrM1VSdnZHMnhKZ3FkZDdYdGhWZnhyQVptdmVvMyIsImFsZyI6IlJTMjU2In0.eyJqdGkiOiJKV1QiLCJhdWQiOiJrZXlzdG9uZSIsImlzcyI6Im0ybS10b2tlbi1zZXJ2aWNlIiwic3ViIjoicHVibGljX3NoYXJlIiwic2NvcGVzIjoibmFzX3JlYWRfb25seSBuYXNfcmVhZF93cml0ZSIsImN1c3RvbUNsYWltcyI6eyJkZXZpY2VJZCI6ImRhYWE3ZmY4LTlhMjctNDE5Mi05Nzk2LTM0NTA3YTQzMGRkYyIsImF1dGhfaWQiOiI3NzRiNzg0MS04ZjRjLTRmNjYtOTM2Ny05NjA5MWIyMzg3YTUifSwiZXhwIjoxNjk1MDEyOTQzfQ.P5hNPoxNNefRVSs0mQ80mPXV4OywiCIChHM69spn_uLvGEJTv7xyE7PPURKt1emM18RX3IycpEqDtIgBlAEPcxzn07zOXIBNqyAKG94nFo8PKzWdAywGTcBMwSmfiAH5ewwJUM0vQqgT6BK2uKDinv3223SDCi1ydSKh_mYCLdOKT2AWounH10M9TIMT-dBNaNogtA33EE9gzXGmizm6mA3sVClKXS6qiFFZV8RAX65M0_x2oh4i_mDEZv5acI7w2lH2pQoiWF5knnWkKb-0avoLOnYoHH6ago04ii41gpYBk491Vrg3H9ss1jty2JYAFcwXOSriQlW73AThGLg3lg";
	private static final String DEVICE_ID = "daaa7ff8-9a27-4192-9796-34507a430ddc";

	public static final ConfigDownload DIR_01 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.mainFolder("gslrfe3rfg6f3wazjq5candn") //
			.localFolder("/home/orlando/camila/pasta-01") //
			.correlationId(CORRELATION_ID) //
			.build();

	public static final ConfigDownload DIR_02 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("shzyh3frigkvkhbp7ewopuvm") //
			.localFolder("/home/orlando/camila/pasta-02") //
			.build();

	public static final ConfigDownload DIR_03 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("5hovbvrhpiizrbm5gtc5h6al") //
			.localFolder("/home/orlando/camila/pasta-03") //
			.build();

	public static final ConfigDownload DIR_04 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("w42nzzusyrtnmuyzi5mu4t7o") //
			.localFolder("/home/orlando/camila/pasta-04") //
			.build();

	public static final ConfigDownload DIR_05 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("kfimc4yxsv7sadaghrmtyzrv") //
			.localFolder("/home/orlando/camila/pasta-05") //
			.build();

	public static final ConfigDownload DIR_06 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("feqy7hmrhwg5lrlwcyp2p3qi") //
			.localFolder("/home/orlando/camila/pasta-06") //
			.build();

	public static final ConfigDownload DIR_07 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("sgetqkx6wk4xkvsqjppbdjiq") //
			.localFolder("/home/orlando/camila/pasta-07") //
			.build();

	public static final ConfigDownload DIR_08 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("y44wgp4n766iouls6pbolh5v") //
			.localFolder("/home/orlando/camila/pasta-08") //
			.build();

	public static final ConfigDownload DIR_09 = ConfigDownload.builder() //
			.baseUrl(BASE_URL) //
			.deviceId(DEVICE_ID) //
			.accessToken(ACCESS_TOKEN) //
			.correlationId(CORRELATION_ID) //
			.mainFolder("5huwiyeyo7rgm5lri7jegwha") //
			.localFolder("/home/orlando/camila/pasta-09") //
			.build();

}
