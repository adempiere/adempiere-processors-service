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
package org.spin.template.controller;

import org.compiere.util.CLogger;
import org.spin.proto.template_service.CreateEntityRequest;
import org.spin.proto.template_service.DeleteEntitiesBatchRequest;
import org.spin.proto.template_service.DeleteEntityRequest;
import org.spin.proto.template_service.Entity;
import org.spin.proto.template_service.GetEntityRequest;
import org.spin.proto.template_service.ListEntitiesRequest;
import org.spin.proto.template_service.ListEntitiesResponse;
import org.spin.proto.template_service.TemplateServiceGrpc.TemplateServiceImplBase;
import org.spin.template.service.Service;
import org.spin.proto.template_service.UpdateEntityRequest;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class TemplateService extends TemplateServiceImplBase {
	
	/**	Logger			*/
	private CLogger log = CLogger.getCLogger(TemplateService.class);
	
	@Override
	public void getEntity(GetEntityRequest request, StreamObserver<Entity> responseObserver) {
		try {
			Entity.Builder entityValue = Service.getEntity(request);
			responseObserver.onNext(entityValue.build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			responseObserver.onError(Status.INTERNAL
					.withDescription(e.getLocalizedMessage())
					.withCause(e)
					.asRuntimeException());
		}
	}
	
	@Override
    public void createEntity(CreateEntityRequest request, StreamObserver<Entity> responseObserver) {
    	try {
    		log.fine("createEntity: " + request);
    		responseObserver.onNext(Service.createEntity(request).build());
    		responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(Status.INTERNAL
				.withDescription(e.getLocalizedMessage())
				.withCause(e)
				.asRuntimeException());
		}
    }
    
    @Override
    public void updateEntity(UpdateEntityRequest request, StreamObserver<Entity> responseObserver) {
    	try {
    		log.fine("updateEntity: " + request);
    		responseObserver.onNext(Service.updateEntity(request).build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(Status.INTERNAL
				.withDescription(e.getLocalizedMessage())
				.withCause(e)
				.asRuntimeException());
		}
    }
    
    @Override
    public void deleteEntity(DeleteEntityRequest request, StreamObserver<Empty> responseObserver) {
    	try {
    		log.fine("deleteEntity: " + request);
    		responseObserver.onNext(Service.deleteEntity(request).build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(Status.INTERNAL
				.withDescription(e.getLocalizedMessage())
				.withCause(e)
				.asRuntimeException());
		}
    }
    
    @Override
	public void deleteEntitiesBatch(DeleteEntitiesBatchRequest request, StreamObserver<Empty> responseObserver) {
		try {
			Empty.Builder entityValue = Service.deleteEntities(request);
			responseObserver.onNext(entityValue.build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			responseObserver.onError(Status.INTERNAL
					.withDescription(e.getLocalizedMessage())
					.withCause(e)
					.asRuntimeException());
		}
	}
	
	@Override
	public void listEntities(ListEntitiesRequest request, StreamObserver<ListEntitiesResponse> responseObserver) {
		try {
			ListEntitiesResponse.Builder entityValueList = Service.listEntities(request);
			responseObserver.onNext(entityValueList.build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			e.printStackTrace();
			responseObserver.onError(Status.INTERNAL
				.withDescription(e.getLocalizedMessage())
				.withCause(e)
				.asRuntimeException()
			);
		}
	}
}