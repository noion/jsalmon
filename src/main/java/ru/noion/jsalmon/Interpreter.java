package ru.noion.jsalmon;

public class Interpreter implements Expr.Visitor<Object> {
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        var left = evaluate(expr.left);
        var right = evaluate(expr.right);

        return switch (expr.operator.type()) {
            case MINUS -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left - (double) right;
            }
            case SLASH -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left / (double) right;
            }
            case STAR -> {
                checkNumberOperand(expr.operator, left, right);
                yield (double) left * (double) right;
            }
            case PLUS -> {
                if (left instanceof Double l && right instanceof Double r) {
                    yield l + r;
                }
                if (left instanceof String l && right instanceof String r) {
                    yield l + r;
                }
                if (left instanceof String l) {
                    yield l + stringify(right);
                }
                if (right instanceof String r) {
                    yield stringify(left) + r;
                }
                yield null;
            }
            case GREATER -> {
                checkNumberOperand(expr.operator, left);
                yield (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperand(expr.operator, left);
                yield (double) left >= (double) right;
            }
            case LESS -> {
                checkNumberOperand(expr.operator, left);
                yield (double) left < (double) right;
            }
            case LESS_EQUAL -> {
                checkNumberOperand(expr.operator, left);
                yield (double) left <= (double) right;
            }
            case BANG_EQUAL -> !isEqual(left, right);
            case EQUAL -> isEqual(left, right);
            default -> null;
        };
    }

    private void checkNumberOperand(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null) {
            return false;
        }
        return left.equals(right);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expression) {
        return expression.accept(this);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        var right = evaluate(expr.right);

        return switch (expr.operator.type()) {
            case BANG -> !isTruthy(right);
            case MINUS -> {
                checkNumberOperand(expr.operator, right);
                yield -(double) right;
            }
            default -> null;
        };

    }

    private void checkNumberOperand(Token operator, Object right) {
        if (right instanceof Double) {
            return;
        }
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private boolean isTruthy(Object right) {
        if (right == null) {
            return false;
        }
        if (right instanceof Boolean) {
            return (boolean) right;
        }
        return true;
    }

    void interpret(Expr expression) {
        try {
            var value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Salmon.runtimeError(error);
        }
    }

    private String stringify(Object value) {
        if (value == null) {
            return "nil";
        }
        if (value instanceof Double) {
            var text = value.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return value.toString();
    }
}
