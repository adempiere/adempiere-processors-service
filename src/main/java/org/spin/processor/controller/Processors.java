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
package org.spin.processor.controller;

import org.compiere.util.CLogger;
import org.spin.proto.processor.RunProcessorRequest;
import org.spin.proto.processor.RunProcessorResponse;
import org.spin.proto.processor.SystemInfo;
import org.spin.processor.service.Service;
import org.spin.proto.processor.GetSystemInfoRequest;
import org.spin.proto.processor.ProcessorsGrpc.ProcessorsImplBase;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class Processors extends ProcessorsImplBase {
	
	/**	Logger			*/
	private CLogger log = CLogger.getCLogger(Processors.class);


	@Override
	public void getSystemInfo(GetSystemInfoRequest request, StreamObserver<SystemInfo> responseObserver) {
		try {
			SystemInfo.Builder builder = Service.getSystemInfo();
			responseObserver.onNext(builder.build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			responseObserver.onError(Status.INTERNAL
				.withDescription(e.getLocalizedMessage())
				.withCause(e)
				.asRuntimeException()
			);
		}
	}

	@Override
	public void runAccounting(RunProcessorRequest request, StreamObserver<RunProcessorResponse> responseObserver) {
		try {
			RunProcessorResponse.Builder response = Service.runAccounting(request.getClientId(), request.getId());
			responseObserver.onNext(response.build());
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
	public void runAlert(RunProcessorRequest request, StreamObserver<RunProcessorResponse> responseObserver) {
		try {
			RunProcessorResponse.Builder response = Service.runAlert(request.getClientId(), request.getId());
			responseObserver.onNext(response.build());
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
	public void runProject(RunProcessorRequest request, StreamObserver<RunProcessorResponse> responseObserver) {
		try {
			RunProcessorResponse.Builder response = Service.runProject(request.getClientId(), request.getId());
			responseObserver.onNext(response.build());
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
	public void runRequest(RunProcessorRequest request, StreamObserver<RunProcessorResponse> responseObserver) {
		try {
			RunProcessorResponse.Builder response = Service.runRequest(request.getClientId(), request.getId());
			responseObserver.onNext(response.build());
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
	public void runScheduler(RunProcessorRequest request, StreamObserver<RunProcessorResponse> responseObserver) {
		try {
			RunProcessorResponse.Builder response = Service.runScheduler(request.getClientId(), request.getId());
			responseObserver.onNext(response.build());
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
	public void runWorkflow(RunProcessorRequest request, StreamObserver<RunProcessorResponse> responseObserver) {
		try {
			RunProcessorResponse.Builder response = Service.runWorkflow(request.getClientId(), request.getId());
			responseObserver.onNext(response.build());
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			responseObserver.onError(Status.INTERNAL
					.withDescription(e.getLocalizedMessage())
					.withCause(e)
					.asRuntimeException());
		}
	}
}