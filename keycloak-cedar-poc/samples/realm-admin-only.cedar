permit(principal, action, resource);

forbid(
    principal,
    action,
    resource
) when {
    context.resourceType == "REALM"
} unless {
    principal == Keycloak::User::"admin-user-id-here"
};
