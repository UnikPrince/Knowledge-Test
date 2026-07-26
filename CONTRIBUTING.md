# Spring Boot Interview Simulator - Contributing Guide

## Code Standards

### Java Code Style
- Follow Google Java Style Guide
- Use 4-space indentation
- Max line length: 120 characters
- Always use `@Override` annotation
- Use meaningful variable names

### Naming Conventions
- Classes: PascalCase (e.g., `InterviewSession`)
- Methods: camelCase (e.g., `generateQuestion`)
- Constants: UPPER_SNAKE_CASE
- Private fields: prefix with underscore (e.g., `_sessionId`)

### Documentation
- Write Javadoc for all public methods
- Include @param, @return, @throws tags
- Document complex logic with inline comments
- Update README for new features

## Git Workflow

### Branch Naming
- Feature: `feature/short-description`
- Bug fix: `fix/issue-number-description`
- Hotfix: `hotfix/critical-issue`
- Release: `release/version-number`

### Commit Messages
```
[TYPE] Short description (max 50 chars)

Detailed explanation (max 72 chars per line)

Related issues: #123, #456
```

Types: feat, fix, docs, style, refactor, perf, test, chore

## Testing

### Test Coverage
- Minimum 80% code coverage
- Test all public methods
- Test error scenarios
- Use meaningful test names

### Test Structure
```java
@Test
public void testMethodNameWhenConditionThenExpected() {
    // Given
    // When
    // Then
}
```

## Pull Request Process

1. Create feature branch from `develop`
2. Make changes and commit regularly
3. Ensure tests pass: `mvn test`
4. Run code quality checks: `mvn checkstyle:check`
5. Push to remote: `git push origin feature/name`
6. Create PR with clear description
7. Address review comments
8. Merge after approval

## Issue Reporting

Include:
- Clear title
- Environment details
- Steps to reproduce
- Expected vs actual behavior
- Error logs/screenshots

---

Thank you for contributing!
