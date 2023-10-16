/************************************************************************************
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                     *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                     *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.template.grpc.client;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.compiere.util.CLogger;
import org.spin.proto.template_service.Entity;
import org.spin.proto.template_service.CreateEntityRequest;
import org.spin.proto.template_service.DeleteEntityRequest;
import org.spin.proto.template_service.TemplateServiceGrpc;
import org.spin.proto.template_service.TemplateServiceGrpc.TemplateServiceBlockingStub;
import org.spin.service.grpc.authentication.TokenManager;
import org.spin.service.grpc.util.ValueManager;
import org.spin.template.setup.SetupLoader;

import com.google.protobuf.Struct;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class TemplateClient {
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(TemplateClient.class);
	
    public static void main(String[] args) throws Exception {
    	if (args == null) {
			throw new Exception("Arguments Not Found");
		}
		//
		if (args.length == 0) {
			throw new Exception("Arguments Must Be: [property file name]");
		}
		String setupFileName = args[0];
		if(setupFileName == null || setupFileName.trim().length() == 0) {
			throw new Exception("Setup File not found");
		}
		SetupLoader.loadSetup(setupFileName);
		//	Validate load
		SetupLoader.getInstance().validateLoad();
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SetupLoader.getInstance().getServer().getHost(),  
                		SetupLoader.getInstance().getServer().getPort())
                .usePlaintext()
                .build();
        TemplateServiceBlockingStub 
                 client = TemplateServiceGrpc
                 .newBlockingStub(channel);
        String testToken = "Bearer ";
        byte[] keyBytes = Decoders.BASE64.decode(testToken);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        TokenManager token = new TokenManager(Jwts.builder()
        		.setId("101")
        		.setAudience("102")
                .setSubject("11")
                .setIssuer("1015340")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact());
        client.withCallCredentials(token);
        try {
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	long start = System.currentTimeMillis();
        	List<Integer> ids = new ArrayList<>();
        	log.warning("Started at: " + format.format(new Date(start)));
        	IntStream.range(0, 100).forEach(index -> {
        		String uuid = UUID.randomUUID().toString();
        		Struct.Builder attributes = Struct.newBuilder();
        		attributes
        			.putFields("Value", ValueManager.getValueFromObject(uuid).build())
        			.putFields("Name", ValueManager.getValueFromObject("Test for gRPC " + index).build())
        			.putFields("Description", ValueManager.getValueFromObject("This is a test based on gRPC " + index).build())
        			.putFields("IsDefault", ValueManager.getValueFromObject(false).build())
        			.putFields("Value", ValueManager.getValueFromObject(uuid).build())
        			.putFields("Value", ValueManager.getValueFromObject(uuid).build())
        			;
        		Entity entity = client.createEntity(CreateEntityRequest.newBuilder()
            			.setTableName("M_Product_Class")
            			.setAttributes(attributes)
            			.build()
        		);
        		ids.add(entity.getId());
                System.out.println("Entity Created " + entity.getId());
        	});
        	ids.forEach(id -> {
        		client.deleteEntity(DeleteEntityRequest.newBuilder()
            			.setTableName("M_Product_Class")
            			.setId(id)
            			.build());
        		System.out.println("Entity Deleted " + id);
        	});
        	long end = System.currentTimeMillis();
        	log.warning("Started at: " + format.format(new Date(start)));
        	log.warning("Finish at: " + format.format(new Date(end)));
            System.out.println("Entities Created");
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
        }
    }
}
