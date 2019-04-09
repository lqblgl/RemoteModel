package com.wy.remote.remotemodel.pool;

import android.os.IBinder;
import android.os.RemoteException;

public class BinderPoolImpl extends IBinderPool.Stub{
	public static final int BINDER_COMPUTE = 0;
	public static final int BINDER_SECURITY_CENTER = 1;
	@Override
	public IBinder queryBinder(int bindercode) throws RemoteException {
		IBinder binder = null;
		switch (bindercode) {
		case BINDER_SECURITY_CENTER:
			binder = new SecurityCenterImpl();
			break;
		case BINDER_COMPUTE:
			binder = new ComputeImpl();
			break;
		default:
			break;
		}
		return binder;
	}
}
