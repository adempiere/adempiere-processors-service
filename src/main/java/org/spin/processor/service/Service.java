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
package org.spin.processor.service;

import java.util.Optional;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MProcess;
import org.compiere.model.MRole;
import org.compiere.process.ProcessInfo;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.eevolution.services.dsl.ProcessBuilder;
import org.spin.base.Version;
import org.spin.eca46.process.AcctProcessor;
import org.spin.eca46.process.AlertProcessor;
import org.spin.eca46.process.ProjectProcessor;
import org.spin.eca46.process.RequestProcessor;
import org.spin.eca46.process.SchedulerProcessor;
import org.spin.eca46.process.WorkflowProcessor;
import org.spin.proto.processor.ProcessorLog;
import org.spin.proto.processor.RunProcessorResponse;
import org.spin.proto.processor.SystemInfo;
import org.spin.service.grpc.util.value.TextManager;
import org.spin.service.grpc.util.value.TimeManager;

public class Service {

	public static SystemInfo.Builder getSystemInfo() {
		SystemInfo.Builder builder = SystemInfo.newBuilder();
		// backend info
		builder.setDateVersion(
				TimeManager.getProtoTimestampFromTimestamp(
					TimeManager.getTimestampFromString(
						Version.DATE_VERSION
					)
				)
			)
			.setMainVersion(
				TextManager.getValidString(
					Version.MAIN_VERSION
				)
			)
			.setImplementationVersion(
				TextManager.getValidString(
					Version.IMPLEMENTATION_VERSION
				)
			)
		;
		return builder;
	}

	public static RunProcessorResponse.Builder runAccounting(int clientId, int id) {
		return runProcess(AcctProcessor.getProcessId(), AcctProcessor.C_ACCTPROCESSOR_ID, clientId, id);
	}
	
	public static RunProcessorResponse.Builder runAlert(int clientId, int id) {
		return runProcess(AlertProcessor.getProcessId(), AlertProcessor.AD_ALERTPROCESSOR_ID, clientId, id);
	}
	
	public static RunProcessorResponse.Builder runProject(int clientId, int id) {
		return runProcess(ProjectProcessor.getProcessId(), ProjectProcessor.C_PROJECTPROCESSOR_ID, clientId, id);
	}
	
	public static RunProcessorResponse.Builder runRequest(int clientId, int id) {
		return runProcess(RequestProcessor.getProcessId(), RequestProcessor.R_REQUESTPROCESSOR_ID, clientId, id);
	}
	
	public static RunProcessorResponse.Builder runScheduler(int clientId, int id) {
		return runProcess(SchedulerProcessor.getProcessId(), SchedulerProcessor.AD_SCHEDULER_ID, clientId, id);
	}
	
	public static RunProcessorResponse.Builder runWorkflow(int clientId, int id) {
		return runProcess(WorkflowProcessor.getProcessId(), WorkflowProcessor.AD_WORKFLOWPROCESSOR_ID, clientId, id);
	}
	
	/**
	 * Run the business processor as process
	 * @param processId
	 * @param parameterName
	 * @param clientId
	 * @param id
	 * @return
	 */
	private static RunProcessorResponse.Builder runProcess(int processId, String parameterName, int clientId, int id) {
		//	Get Process definition
		MProcess process = MProcess.get(Env.getCtx(), processId);
		if(process == null
				|| process.getAD_Process_ID() <= 0) {
			throw new AdempiereException("@AD_Process_ID@ @NotFound@");
		}
		RunProcessorResponse.Builder response = RunProcessorResponse.newBuilder();
		;
		if(process == null || process.getAD_Process_ID() <= 0) {
			throw new AdempiereException("@AD_Process_ID@ @NotFound@");
		}
		MRole role = MRole.getDefault();
		if(!Optional.ofNullable(role.getProcessAccess(process.getAD_Process_ID())).orElse(false)) {
			if (process.isReport()) {
				throw new AdempiereException("@AccessCannotReport@");
			}
			throw new AdempiereException("@AccessCannotProcess@");
		}
		Env.setContext(Env.getCtx(), "#AD_Client_ID", clientId);
		//	Call process builder
		ProcessBuilder builder = ProcessBuilder.create(Env.getCtx())
				.process(process.getAD_Process_ID())
				.withoutPrintPreview()
				.withoutBatchMode()
				.withWindowNo(0)
				.withTitle(process.getName())
				.withParameter(parameterName, id);
		//	Execute Process
		ProcessInfo result = null;
		try {
			result = builder.execute();
		} catch (Exception e) {
			result = builder.getProcessInfo();
			//	Set error message
			String summary = Msg.parseTranslation(Env.getCtx(), result.getSummary());
			if(Util.isEmpty(summary, true)) {
				result.setSummary(
					TextManager.getValidString(
						e.getLocalizedMessage()
					)
				);
			}
		}
		//	
		response.setIsError(result.isError());
		if(!Util.isEmpty(result.getSummary())) {
			response.setSummary(Msg.parseTranslation(Env.getCtx(), result.getSummary()));
		}
		//	Convert Log
		if(result.getLogList() != null) {
			for(org.compiere.process.ProcessInfoLog log : result.getLogList()) {
				ProcessorLog.Builder infoLogBuilder = Converter.convertProcessorLog(log);
				response.addLogs(infoLogBuilder.build());
			}
		}
		return response;
	}
}
