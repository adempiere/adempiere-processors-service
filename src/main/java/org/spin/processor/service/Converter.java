/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * This program is free software; you can redistribute it and/or modify it    		 *
 * under the terms version 2 or later of the GNU General Public License as published *
 * by the Free Software Foundation. This program is distributed in the hope   		 *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 		 *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           		 *
 * See the GNU General Public License for more details.                       		 *
 * You should have received a copy of the GNU General Public License along    		 *
 * with this program; if not, write to the Free Software Foundation, Inc.,    		 *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     		 *
 * For the text or an alternative of this public license, you may reach us    		 *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpya.com				  		                 *
 *************************************************************************************/
package org.spin.processor.service;

import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.spin.proto.processor.ProcessorLog;
import org.spin.service.grpc.util.value.ValueManager;

/**
 * Class for convert any document
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class Converter {
	/**
	 * Convert ProcessInfoLog to gRPC
	 * @param log
	 * @return
	 */
	public static ProcessorLog.Builder convertProcessorLog(org.compiere.process.ProcessInfoLog log) {
		ProcessorLog.Builder processLog = ProcessorLog.newBuilder();
		if (log == null) {
			return processLog;
		}
		processLog.setId(log.getP_ID());
		processLog.setLog(ValueManager.validateNull(Msg.parseTranslation(Env.getCtx(), log.getP_Msg())));
		return processLog;
	}
}
