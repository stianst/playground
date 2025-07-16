# GitHub publishing

No need for any Maven plugins/extensions:

```
mvn clean deploy -DaltDeploymentRepository=github::https://maven.pkg.github.com/stianst/playground -DretryFailedDeploymentCount=10
```