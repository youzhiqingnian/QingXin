package com.vlee78.android.vl;

import java.util.Iterator;

public class VLModelManager 
{
	private VLListMap<String, VLModel> mModels;
	private boolean mCanGetModel;

	public VLModelManager()
	{
		mCanGetModel = false;
		mModels = new VLListMap<>();
	}

	public <T> T registerModel(Class<T> modelClass)
	{
		String className = modelClass.getName();
		VLModel model = VLUtils.classNew(className);
		VLDebug.Assert(model != null);
		mModels.addTail(className, model);
		return (T)model;
	}

	public Iterator<? extends VLModel> getManagedModels() {
		return mModels.values();
	}

	public void createAndInitModels()
	{
		for (Iterator<VLModel> it = mModels.values(); it.hasNext();)
		{
			VLModel model = it.next();
			model.onCreate();
		}
		mCanGetModel = true;

		for (Iterator<VLModel> it = mModels.values(); it.hasNext();)
		{
			VLModel model = it.next();
			model.onBeforeAfterCreate();
		}

		for (Iterator<VLModel> it = mModels.values(); it.hasNext();)
		{
			VLModel model = it.next();
			model.onAfterCreate();
		}


		VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock()
		{
			@Override
			protected void process(boolean canceled) {
				for (Iterator<VLModel> it = mModels.values(); it.hasNext();) {
					VLModel model = it.next();
					model.onAfterAfterCreate();
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	public <T> T getModel(Class<T> modelClass)
	{
		if(!mCanGetModel) VLUtils.RE("getModel must called after onCreate in onAfterCreate");
		if(modelClass == null) return null;
		String className = modelClass.getName();
		VLModel model = mModels.get(className);
		return (T)model;
	}
}
