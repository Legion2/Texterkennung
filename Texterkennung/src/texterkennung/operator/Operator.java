package texterkennung.operator;

import java.util.function.Supplier;

import debug.IInfo;
import texterkennung.data.Data;

public interface Operator<T extends Data> extends IInfo, Supplier<T>
{
	
}
