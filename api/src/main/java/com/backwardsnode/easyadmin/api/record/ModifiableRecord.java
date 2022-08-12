/*
 * MIT License
 *
 * Copyright (c) 2022 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.backwardsnode.easyadmin.api.record;

import com.backwardsnode.easyadmin.api.record.modify.RecordModifier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a record that can be modified and then saved.
 * @param <S> the wrapper responsible for making modifications to the record.
 * @param <T> the type the wrapper should need to copy.
 */
public interface ModifiableRecord<S extends RecordModifier, T extends LiveRecord<?>> {

    /**
     * Creates a new {@link RecordModifier} wrapper around the current record for modifications.
     * @return the new wrapper.
     */
    @NotNull S getModifiableRecord();

    /**
     * Copies the current record object into a new one to make modifications to.
     * @return a copy of the current record.
     */
    @NotNull T copy();
}
