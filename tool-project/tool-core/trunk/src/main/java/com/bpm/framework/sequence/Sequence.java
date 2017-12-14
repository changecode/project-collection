package com.bpm.framework.sequence;

import java.io.Serializable;

public interface Sequence extends Serializable {

	public String next(String prefix);
}
