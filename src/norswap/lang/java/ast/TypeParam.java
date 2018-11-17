package norswap.lang.java.ast;

import com.google.auto.value.AutoValue;
import java.lang.annotation.Annotation;
import java.util.List;

@AutoValue
public abstract class TypeParam
{
    public abstract List<Annotation> annotations();
    public abstract String name();
    public abstract List<TType> bounds();

    public static TypeParam make (List<Annotation> annotations, String name, List<TType> bounds) {
        return new AutoValue_TypeParam(annotations, name, bounds);
    }
}