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
package org.spin.template.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.adempiere.core.domains.models.X_AD_Table;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.compiere.util.Util;
import org.spin.proto.template_service.CreateEntityRequest;
import org.spin.proto.template_service.DeleteEntitiesBatchRequest;
import org.spin.proto.template_service.DeleteEntityRequest;
import org.spin.proto.template_service.Entity;
import org.spin.proto.template_service.GetEntityRequest;
import org.spin.proto.template_service.ListEntitiesRequest;
import org.spin.proto.template_service.ListEntitiesResponse;
import org.spin.proto.template_service.UpdateEntityRequest;
import org.spin.service.grpc.util.ValueManager;

import com.google.protobuf.Empty;
import com.google.protobuf.Value;

public class Service {
	
	/** Table Allows Records with Zero Identifier */
	public static List<String> ALLOW_ZERO_ID = Arrays.asList(
		X_AD_Table.ACCESSLEVEL_All,
		X_AD_Table.ACCESSLEVEL_SystemPlusClient,
		X_AD_Table.ACCESSLEVEL_ClientPlusOrganization
	);
	
	/**
	 * Create Entity
	 * @param context
	 * @param request
	 * @return
	 */
	public static Entity.Builder createEntity(CreateEntityRequest request) {
		if(Util.isEmpty(request.getTableName())) {
			throw new AdempiereException("@AD_Table_ID@ @NotFound@");
		}
		MTable table = MTable.get(Env.getCtx(), request.getTableName());
		if (table == null || table.getAD_Table_ID() <= 0) {
			throw new AdempiereException("@AD_Table_ID@ @NotFound@");
		}
		PO entity = table.getPO(0, null);
		if(entity == null) {
			throw new AdempiereException("@Error@ PO is null");
		}
		Map<String, Value> attributes = request.getAttributes().getFieldsMap();
		attributes.keySet().forEach(key -> {
			Value attribute = attributes.get(key);
			Object value = ValueManager.getObjectFromValue(attribute);
			entity.set_ValueOfColumn(key, value);
		});
		//	Save entity
		entity.saveEx();
		//	Return
		return Converter.convertEntity(entity);
	}
	
	
	/**
	 * Update Entity
	 * @param context
	 * @param request
	 * @return
	 */
	public static Entity.Builder updateEntity(UpdateEntityRequest request) {
		PO entity = getEntity(request.getTableName(), request.getId(), null);
		if(entity != null
				&& entity.get_ID() >= 0) {
			Map<String, Value> attributes = request.getAttributes().getFieldsMap();
			attributes.keySet().forEach(key -> {
				Value attribute = attributes.get(key);
				Object value = ValueManager.getObjectFromValue(attribute);
				entity.set_ValueOfColumn(key, value);
			});
			//	Save entity
			entity.saveEx();
		}
		//	Return
		return Converter.convertEntity(entity);
	}
	
	/**
	 * Convert a PO from query
	 * @param request
	 * @return
	 */
	public static Entity.Builder getEntity(GetEntityRequest request) {
		String tableName = request.getTableName();
		PO entity = null;
		if(request.getId() != 0) {
			entity = getEntity(tableName, request.getId(), null);
		}
//		else if(request.getFilters() != null) {
//			List<Object> parameters = new ArrayList<Object>();
//			String whereClause = WhereClauseUtil.getWhereClauseFromCriteria(request.getFilters(), parameters);
//			entity = RecordUtil.getEntity(Env.getCtx(), tableName, whereClause, parameters, null);
//		}
		//	Return
		return Converter.convertEntity(entity);
	}
	
	/**
	 * Convert Object to list
	 * @param request
	 * @return
	 */
	public static ListEntitiesResponse.Builder listEntities(ListEntitiesRequest request) {
//		StringBuffer whereClause = new StringBuffer();
//		List<Object> params = new ArrayList<>();
		//	For dynamic condition
//		String dynamicWhere = WhereClauseUtil.getWhereClauseFromCriteria(request.getFilters(), params);
//		if(!Util.isEmpty(dynamicWhere)) {
//			if(whereClause.length() > 0) {
//				whereClause.append(" AND ");
//			}
//			//	Add
//			whereClause.append(dynamicWhere);
//		}
		//	TODO: Add support to this functionality with a distinct scope
		//	Add from reference
//		if(!Util.isEmpty(criteria.getReferenceUuid())) {
//			String referenceWhereClause = referenceWhereClauseCache.get(criteria.getReferenceUuid());
//			if(!Util.isEmpty(referenceWhereClause)) {
//				if(whereClause.length() > 0) {
//					whereClause.append(" AND ");
//				}
//				whereClause.append("(").append(referenceWhereClause).append(")");
//			}
//		}
		//	Get page and count
//		String nexPageToken = null;
//		int pageNumber = LimitUtil.getPageNumber(SessionManager.getSessionUuid(), request.getPageToken());
//		int limit = LimitUtil.getPageSize(request.getPageSize());
//		int offset = (pageNumber - 1) * limit;
//		int count = 0;
//
		ListEntitiesResponse.Builder builder = ListEntitiesResponse.newBuilder();
//		Query query = new Query(context, request.getTableName(), whereClause.toString(), null)
//				.setParameters(params);
//		count = query.count();
//		if(!Util.isEmpty(request.getSortBy())) {
//			query.setOrderBy(SortingManager.newInstance(request.getSortBy()).getSotingAsSQL());
//		}
//		List<PO> entityList = query
//				.setLimit(limit, offset)
//				.<PO>list();
//		//	
//		for(PO entity : entityList) {
//			Entity.Builder valueObject = ConvertUtil.convertEntity(entity);
//			builder.addRecords(valueObject.build());
//		}
//		//	
//		builder.setRecordCount(count);
//		//	Set page token
//		if(LimitUtil.isValidNextPageToken(count, offset, limit)) {
//			nexPageToken = LimitUtil.getPagePrefix(SessionManager.getSessionUuid()) + (pageNumber + 1);
//		}
//		//	Set netxt page
//		builder.setNextPageToken(ValueUtil.validateNull(nexPageToken));
		//	Return
		return builder;
	}
	
	/**
	 * Delete many entities
	 * @param context
	 * @param request
	 * @return
	 */
	public static Empty.Builder deleteEntities(DeleteEntitiesBatchRequest request) {
		Trx.run(transactionName -> {
			if(Util.isEmpty(request.getTableName())) {
				throw new AdempiereException("@AD_Table_ID@ @NotFound@");
			}
			List<Integer> ids = request.getIdsList();
			if (ids.size() > 0) {
				MTable table = MTable.get(Env.getCtx(), request.getTableName());
				ids.stream().forEach(id -> {
					PO entity = table.getPO(id, transactionName);
					if (entity != null && entity.get_ID() > 0) {
						entity.deleteEx(true);
					}
				});
			}
		});
		//	Return
		return Empty.newBuilder();
	}
	
	/**
	 * Delete a entity
	 * @param context
	 * @param request
	 * @return
	 */
	public static Empty.Builder deleteEntity(DeleteEntityRequest request) {
		if (request.getId() > 0) {
			PO entity = getEntity(request.getTableName(), request.getId(), null);
			if (entity != null && entity.get_ID() > 0) {
				entity.deleteEx(true);
			}
		}
		//	Return
		return Empty.newBuilder();
	}
	
	/**
	 * Evaluate if is valid identifier
	 * @param id
	 * @param accesLevel
	 * @return
	 */
	public static boolean isValidId(int id, String accesLevel) {
		if (id < 0) {
			return false;
		}

		if (id == 0 && !ALLOW_ZERO_ID.contains(accesLevel)) {
			return false;
		}

		return true;
	}
	
	/**
	 * Get PO entity from Table and record id
	 * @param tableName
	 * @param id
	 * @return
	 */
	public static PO getEntity(String tableName, int id, String transactionName) {
		if (Util.isEmpty(tableName, true)) {
			throw new AdempiereException("@FillMandatory@ @AD_Table_ID@");
		}
		MTable table = MTable.get(Env.getCtx(), tableName);
		if (table == null || table.getAD_Table_ID() <= 0) {
			throw new AdempiereException("@AD_Table_ID@ @NotFound@");
		}
		//	Validate ID
		if (!isValidId(id, table.getAccessLevel())) {
			throw new AdempiereException("@FillMandatory@ @Record_ID@");
		}
		return MTable.get(Env.getCtx(), tableName).getPO(id, transactionName);
	}
}
