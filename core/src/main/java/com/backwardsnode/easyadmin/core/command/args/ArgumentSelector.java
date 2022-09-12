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

package com.backwardsnode.easyadmin.core.command.args;

import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.data.AddressType;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.core.Utils;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public final class ArgumentSelector {

	private final EasyAdminPlugin instance;
	private final String[] args;
	private int argsRead = 0;
	private int argsProcessed = 0;
	private int lastAdvance = -1;

	public ArgumentSelector(EasyAdminPlugin instance, String[] args) {
		this.instance = instance;
		this.args = args;
	}

	private int advance(int count) {
		int current = argsRead;

		argsRead += count;
		argsProcessed++;
		lastAdvance = count;

		return current;
	}

	public ArgumentSelector undo() {
		if (lastAdvance == -1) {
			throw new IllegalStateException("No read history.");
		}

		argsRead -= lastAdvance;
		argsProcessed--;
		lastAdvance = -1;

		return this;
	}

	public ArgumentResult<String> readSingleArgument() {
		if (argsRead >= args.length) {
			return ArgumentResult.failure();
		}

		int i = argsRead;
		advance(1);
		return ArgumentResult.of(args[i]);
	}

	public ArgumentResult<OfflinePlayer> readPlayerName() {
		if (argsRead >= args.length) {
			return ArgumentResult.failure();
		}

		int i = advance(1);
		return ArgumentResult.of(instance.getOfflinePlayer(args[i]));
	}

	public ArgumentResult<OnlinePlayer> readOnlinePlayerName() {
		if (argsRead >= args.length) {
			return ArgumentResult.failure();
		}

		int i = advance(1);
		return ArgumentResult.of(instance.getOnlinePlayer(args[i]));
	}

	public ArgumentResult<Duration> readDuration() {
		if (argsRead >= args.length) {
			return ArgumentResult.failure();
		}

		try {
			int i = advance(1);
			return ArgumentResult.of(Utils.parseDuration(args[i]));
		} catch (DateTimeParseException e) {
			return ArgumentResult.failure();
		}
	}

	public ArgumentResult<String> readNetworkAddress() {
		if (argsRead >= args.length) {
			return ArgumentResult.failure();
		}
		int i = advance(1);
		return ArgumentResult.onlyIf(args[i], x -> Utils.getAddressType(x) != AddressType.UNKNOWN);
	}
	
	public int getSourceArgsRead() {
		return argsRead;
	}

	public int getArgsProcessed() {
		return argsProcessed;
	}
	
	public String readRemainingString() {
		if (args.length > argsRead) {
			StringBuilder sb = new StringBuilder();
			for (int i = argsRead; i < args.length; i++) {
				sb.append(args[i]);
				if (i < args.length - 1) {
					sb.append(" ");
				}
			}
			return sb.toString();
		}
		return "";
		
	}
}
