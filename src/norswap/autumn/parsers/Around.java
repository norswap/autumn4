package norswap.autumn.parsers;

import norswap.autumn.DSL.rule;
import norswap.autumn.Parse;
import norswap.autumn.Parser;
import norswap.autumn.ParserVisitor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Matches a series of repetition of a subparser (the *around* parser), separated by another parser
 * (the *inside* parser). See {@link #Around} for more details.
 *
 * <p>Build with {@link rule#sep(int, Object)}, {@link rule#sep_trailing(int, Object)} or {@link
 * rule#sep_exact(int, Object)}.
 */
public final class Around extends Parser
{
    // ---------------------------------------------------------------------------------------------

    public final int min;

    // ---------------------------------------------------------------------------------------------

    public final boolean exact;

    // ---------------------------------------------------------------------------------------------

    public final boolean trailing;

    // ---------------------------------------------------------------------------------------------

    public final Parser around;

    // ---------------------------------------------------------------------------------------------

    public final Parser inside;

    // ---------------------------------------------------------------------------------------------

    private final Parser inside_then_around;

    // ---------------------------------------------------------------------------------------------

    /**
     * This parser will matches at least {@code min} repetitions of {@code around}, separated by
     * matches for {@code inside}. If {@code exact} is true, will match exactly {@code min}
     * repetitions. Otherwise, matches as many repetitions as possible. If {@code trailing} is true,
     * allows an optional trailing match for {@code inside}.
     */
    public Around (int min, boolean exact, boolean trailing, Parser around, Parser inside)
    {
        this.min = min;
        this.exact = exact;
        this.trailing = trailing;
        this.around = around;
        this.inside = inside;
        this.inside_then_around = new Sequence(inside, around);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public boolean doparse (Parse parse)
    {
        if (!around.parse(parse)) {
            if (min == 0 && trailing)
                inside.parse(parse);
            return min == 0;
        }
        for (int i = 0; i < min - 1; ++i)
            if (!inside_then_around.parse(parse))
                return false;
        if (!exact)
            while (inside_then_around.parse(parse)) ;
        if (trailing)
            inside.parse(parse);
        return true;
    }

    // ---------------------------------------------------------------------------------------------

    @Override public void accept (ParserVisitor visitor) {
        visitor.visit(this);
    }

    // ---------------------------------------------------------------------------------------------

    @Override public List<Parser> children() {
        return Collections.unmodifiableList(Arrays.asList(around, inside));
    }

    // ---------------------------------------------------------------------------------------------

    @Override public String toStringFull()
    {
        StringBuilder b = new StringBuilder();
        b.append("around(");
        b.append(around).append(", ");
        b.append(inside).append(", ");
        b.append(min);
        if (exact)
            b.append(", exact");
        if (trailing)
            b.append(", trailing");
        b.append(")");
        return b.toString();
    }

    // ---------------------------------------------------------------------------------------------
}
