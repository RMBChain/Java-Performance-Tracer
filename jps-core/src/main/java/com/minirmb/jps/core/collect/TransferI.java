package com.minirmb.jps.core.collect;

import java.io.IOException;

public interface TransferI {

	long transfer(StringBuilder data) throws IOException;

	void close() ;

}
