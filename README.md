Requires:

* [GitHub CLI](https://cli.github.com/)

Scripts:

* `kc-workflow-health.sh` - Show % success of workflows (`./kc-workflow-health.sh -d 3` will show % success last 3 days)
* `kc-ci-failures.sh` - Show failures in Keycloak CI workflow (`./kc-ci-failures.sh -d 3 -u` will show unique failures last 3 days)
* `kc-pr-ci-wait-time.sh` - Show waiting time from PR created/updated to CI complets (`./kc-pr-ci-wait-time.sh -d 7` will show wait time for PRs merged last 7 days)
test
