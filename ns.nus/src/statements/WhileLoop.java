/*
 * Copyright (C) 2018-2019  Dinu Blanovschi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package statements;

import tokens.Token;
import tokens.ValueEvaluator;
import tree.Statements;
import variables.Variable;

import java.util.Map;
import java.util.Objects;

public class WhileLoop extends Statement {
	public final Token[] conditionTokens;
	public final Statements statements;

	public WhileLoop(Token[] conditionTokens, Statements statements) {
		super(Statement_TYPE.WHILE_LOOP);
		this.conditionTokens = conditionTokens;
		this.statements = statements;
	}

	@Override
	public void run(Map<String, Variable> variables) {
		while (Objects.requireNonNull(ValueEvaluator.evaluate(conditionTokens, variables)).vi == 1)
			statements.run(variables);
	}
}