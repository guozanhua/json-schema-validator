/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.jsonschema.main.cli;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import joptsimple.HelpFormatter;
import joptsimple.OptionDescriptor;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class CustomHelpFormatter
    implements HelpFormatter
{
    private static final String HELP_PREAMBLE
        = "Syntax: java -jar jsonschema.jar [options] file [file...]";

    private static final String LINE_SEPARATOR
        = System.getProperty("line.separator", "\n");

    private static final Joiner JOINER = Joiner.on(LINE_SEPARATOR);
    static final Joiner OPTIONS_JOINER = Joiner.on(", ");

    private final List<String> lines = Lists.newArrayList();

    @Override
    public String format(final Map<String, ? extends OptionDescriptor> options)
    {
        final Set<OptionDescriptor> opts = new LinkedHashSet<OptionDescriptor>(
            options.values());

        lines.add(HELP_PREAMBLE);
        lines.add("");
        lines.add("Options: ");

        final int helpIndex = lines.size();
        StringBuilder sb;

        for (final OptionDescriptor descriptor : opts) {
            if (descriptor.representsNonOptions())
                continue;
            final Collection<String> names = descriptor.options();
            sb = new StringBuilder().append('\t')
                .append(optionsToString(names));
            if (descriptor.requiresArgument())
                sb.append(" uri");
            sb.append(": ").append(descriptor.description());
            if (names.contains("help"))
                lines.add(helpIndex, sb.toString());
            else
                lines.add(sb.toString());
        }

        lines.add("");
        lines.add("Exit codes:");
        lines.add("\t0: validation successful;");
        lines.add("\t1: exception occurred (appears on stderr)");
        lines.add("\t2: command line syntax error (missing argument, etc)");
        lines.add("\t100: one or more file(s) failed validation");
        lines.add("\t101: one or more schema(s) is/are invalid");

        return JOINER.join(lines) + LINE_SEPARATOR;
    }

    private static String optionsToString(final Collection<String> names)
    {
        final List<String> list = Lists.newArrayList();
        for (final String name : names)
            list.add((name.length() == 1 ? "-" : "--") + name);
        return OPTIONS_JOINER.join(list);
    }
}
