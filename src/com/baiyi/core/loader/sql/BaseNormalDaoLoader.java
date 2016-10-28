package com.baiyi.core.loader.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.baiyi.core.database.AbstractBaseModel;
import com.baiyi.core.database.dao.AbstractNormalDao;
import com.baiyi.core.loader.BaseLoader;
import com.baiyi.core.loader.Loader;
import com.baiyi.core.loader.LoaderResult;
import com.baiyi.core.util.ClassUtil;

@SuppressWarnings("rawtypes")
public abstract class BaseNormalDaoLoader<T extends AbstractNormalDao> extends
		BaseLoader implements Loader {
	private final static String OP_SIMPLE_SELECT = "select";
	private final static String OP_SIMPLE_UPDATE = "update";
	private final static String OP_SIMPLE_DELETE = "delete";
	private final static String OP_ALL_DELETE = "deleteAll";
	private final static String OP_SIMPLE_INSERT = "insert";
	private final static String OP_INSERT_LIST = "insertList";
	private final static String OP_UPDATE_LIST = "updateList";
	private final static String OP_COUNT = "count";
	private final static String OP_INSERT_EXIST_UPDATE_NONE = "insertExistUpdateOrNone";

	protected T dao = null;
	private List<? extends AbstractBaseModel> lists = null;
	private AbstractBaseModel model = null;
	private String opType = null;
	public static String returnMsg = "";

	@SuppressWarnings("unchecked")
	public BaseNormalDaoLoader() {
		Class<?> clazz = ClassUtil.getGenericClass(this.getClass(), 0);
		try {
			dao = (T) clazz.newInstance();
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException("get dao class not match");
		}
	}

	protected AbstractBaseModel getModel() {
		return this.model;
	}

	protected void setModel(AbstractBaseModel model) {
		this.model = model;
	}

	public void setDelete(AbstractBaseModel model) {
		this.model = model;
		this.opType = OP_SIMPLE_DELETE;
	}

	public void setDeleteAll() {
		this.opType = OP_ALL_DELETE;
	}

	public void setInsert(AbstractBaseModel model) {
		this.opType = OP_SIMPLE_INSERT;
		this.model = model;
	}

	public void setInsertExistUpdateOrNone(AbstractBaseModel model,
			boolean isNone) {
		this.opType = OP_INSERT_EXIST_UPDATE_NONE;
		this.model = model;
//		this.isNone = isNone;
	}

	/**
	 * Update
	 * 
	 * @param model
	 */
	public void setUpdate(AbstractBaseModel model) {
		this.opType = OP_SIMPLE_UPDATE;
		this.model = model;
	}

	/**
	 * 查找
	 * 
	 * @param model
	 */
	public void setSelect(AbstractBaseModel model) {
		this.opType = OP_SIMPLE_SELECT;
		this.model = model;
	}

	public void setCount() {
		this.opType = OP_COUNT;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	@SuppressWarnings("unchecked")
	private void delete() {
		dao.delete(model);
	}
	
	private void deleteAll()
	{
		dao.deleteAll();
	}
	
	@SuppressWarnings("unchecked")
	private void insert() {
		dao.insert(model);
	}

	@SuppressWarnings("unchecked")
	private void update() {
		dao.update(model);
	}

	private Long count() {
		return dao.getCount();
	}

	@SuppressWarnings("unchecked")
	private List<AbstractBaseModel> select() {
		List<AbstractBaseModel> result = new ArrayList<AbstractBaseModel>();
		if (model == null) {
			result = dao.getList();
		} else if (model.getId() != null) {
			AbstractBaseModel obj = dao.get("id", model.getId().toString());
			result.add(obj);
		} else {
			Set<String> filedSet = model.toFieldSet();
			ArrayList<String> args = new ArrayList<String>();
			ArrayList<String> selections = new ArrayList<String>();
			JSONObject obj = model.toUpperNameJSON();
			for (String filed : filedSet) {
				try {
					Object value = obj.get(filed);
					if (value != null) {
						selections.add(filed);
						args.add(value.toString());
					}
				} catch (JSONException e) {
				}
			}
			if (selections.size() == 0) {
				result = dao.getList();
			} else {
				StringBuilder builder = new StringBuilder();
				builder.append("WHERE");
				for (int i = 0; i < selections.size(); i++) {
					if (i != 0) {
						builder.append(" ").append("AND");
					}
					builder.append(" ").append(selections.get(i)).append("=")
							.append("?");
				}
				result = dao.getList(builder.toString(),
						args.toArray(new String[1]));
			}
		}
		return result;
	}

	public void setInsertList(List<? extends AbstractBaseModel> lists) {
		this.lists = lists;
		this.opType = OP_INSERT_LIST;
	}

	@SuppressWarnings("unchecked")
	private void insertList() {
		dao.insert(lists);
	}

	public void setUpdateList(List<? extends AbstractBaseModel> lists) {
		this.lists = lists;
		this.opType = OP_UPDATE_LIST;
	}

	@SuppressWarnings("unchecked")
	private void updateList() {
		dao.update(lists);
	}

	protected abstract LoaderResult onVisitor(String opType);

	@Override
	protected LoaderResult onVisitor() {
		if (opType == null) {
			return sendErrorMsg(0, "没有设置操作");
		}
		Object o = null;
		if (opType.equals(OP_SIMPLE_DELETE)) {
			delete();
		} else if(opType.equals(OP_ALL_DELETE))
		{
			deleteAll();
		}else if (opType.equals(OP_SIMPLE_INSERT)) {
			insert();
		} else if (opType.equals(OP_SIMPLE_SELECT)) {
			o = select();
		} else if (opType.equals(OP_SIMPLE_UPDATE)) {
			update();
		} else if (opType.equals(OP_INSERT_LIST)) {
			insertList();
		} else if (opType.equals(OP_UPDATE_LIST)) {
			updateList();
		} else if (opType.equals(OP_COUNT)) {
			o = count();
		} else {
			LoaderResult result = onVisitor(opType);
			if (result.getCode() < 0) {
				return result;
			}
			o = result.getResult();
		}
		if (o != null) {
			setResult(o);
		}
		return sendCompleteMsg();
	}
}
