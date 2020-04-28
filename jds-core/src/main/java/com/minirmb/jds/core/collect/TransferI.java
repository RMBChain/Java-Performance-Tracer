package com.minirmb.jds.core.collect;

import java.io.IOException;

public interface TransferI {

	public long transfer(String data) throws IOException;

	public void close() ;

}
