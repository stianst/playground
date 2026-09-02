permit(
    principal,
    action == Action::"token-exchange",
    resource
);

forbid(
    principal == Client::"myclient2",
    action,
    resource
);

forbid(
    principal,
    action == Action::"token-exchange",
    resource
) when {
    context.aud.contains("aud2")
};