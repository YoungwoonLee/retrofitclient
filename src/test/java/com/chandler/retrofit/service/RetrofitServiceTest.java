package com.chandler.retrofit.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class RetrofitServiceTest {

	@Autowired
	private RetrofitService retrofitService;
	
	@Test
	public void getPingTest() {
		
		Call<ResponseBody> call = retrofitService.getPing();
		
		try {
			
			retrofit2.Response<ResponseBody> response = call.execute();
			if (response.isSuccessful()) {
				
				System.out.println("{성공}: " + response.code());
				
			} else {
				String errorResult = response.errorBody().string();
				ObjectMapper mapper = new ObjectMapper();
				String str = mapper.readValue(errorResult, String.class);
				log.error("{에러}::" + str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void octetStreamUploadedTest1() {
		
		RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), genPacket() );
		
		Call<ResponseBody> call = retrofitService.postOtetStream(1L, 1L, body);
		
		try {
			retrofit2.Response<ResponseBody> response = call.execute();
			
			if (response.isSuccessful()) {
				
				System.out.println("{성공}: " + response.code());
				
			} else {
				String errorResult = response.errorBody().string();
				log.error("{에러}::" + errorResult);
				log.warn("{에러}::" + errorResult);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void octetStreamUploadedTest2() {
		
		RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), genPacket() );
		retrofitService.postOtetStream2(body, new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				System.out.println("{성공}: " + response.code());
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				log.error("{에러}::" + t.getLocalizedMessage());				
			}
		}); 
	}
	
	
	private byte[] genPacket() {
    	String binary = "0000000100000100000001001001100000111010011001000001111101000000100111100110010010100101001110110000011011000010001011100111101111111111111110010110111100010011010010010110111011011100000001001001100000111010011001000001111101000000100111100110010010100101001110110000011011000010001011100111101111111111111110010110111100010011100010010100101101011111000001001001100000111010011001000001111101000000100111100110010010100101001110110000011011000010001011100111101111111111111110010110111100010011100010011001010110111100000001001001100000111010011001000001111101000000100111100110010010100101001110110000011011000010001011100111101111111111111110010110111100010011100010011100111110111100";
    	
    	byte[] packetOrg = binaryStringToByteArray(binary);
    	
    	byte[] p = null;
    	//패킷 압축...
    	try {
			p = this.zLibCompress( packetOrg );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return p;
    }
	/**
     * 바이너리 스트링을 바이트배열로 변환
     * 
     * @param s
     * @return
     */
    private byte[] binaryStringToByteArray(String s) {
//    	BigInteger bi = new BigInteger(s, 2);
//        byte[] b = bi.toByteArray();
        
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }
    /**
     * 바이너리 스트링을 바이트로 변환
     * 
     * @param s
     * @return
     */
    private byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }
    /**
	 * ZLIB 압축 util
	 * @param data
	 * @return
	 * @throws IOException
	 */
    private byte[] zLibCompress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		
		deflater.setInput(data);
//		deflater.setLevel(6); // default compression level = 6
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		
		outputStream.close();
		byte[] output = outputStream.toByteArray();
//		System.out.println("Original	: " + data.length/* / 1024 + " Kb" */);
//		System.out.println("Compressed	: " + output.length /* / 1024 + " Kb" */);
		return output;
	}
}
