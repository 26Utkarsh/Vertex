$ErrorActionPreference = "Stop"

$envFile = Join-Path $PSScriptRoot "..\.env.local"
if (!(Test-Path -LiteralPath $envFile)) {
    throw "Missing backend .env.local file."
}

Get-Content -LiteralPath $envFile | ForEach-Object {
    $line = $_.Trim()
    if ($line.Length -eq 0 -or $line.StartsWith("#")) {
        return
    }
    $separator = $line.IndexOf("=")
    if ($separator -lt 1) {
        throw "Invalid env line: $line"
    }
    $name = $line.Substring(0, $separator)
    $value = $line.Substring($separator + 1)
    Set-Item -Path "Env:$name" -Value $value
}

$env:JWT_SECRET = if ($env:JWT_SECRET) { $env:JWT_SECRET } else { "local-dev-secret-change-before-deploy-123456" }
$env:INTERNAL_API_KEY = if ($env:INTERNAL_API_KEY) { $env:INTERNAL_API_KEY } else { "local-internal-key-change-before-deploy" }
$env:GOOGLE_CLIENT_ID = if ($env:GOOGLE_CLIENT_ID) { $env:GOOGLE_CLIENT_ID } else { "local-google-client-id" }
$env:GOOGLE_CLIENT_SECRET = if ($env:GOOGLE_CLIENT_SECRET) { $env:GOOGLE_CLIENT_SECRET } else { "local-google-client-secret" }
$env:APP_FRONTEND_URL = if ($env:APP_FRONTEND_URL) { $env:APP_FRONTEND_URL } else { "http://localhost:3000" }
$env:ALLOWED_ORIGINS = if ($env:ALLOWED_ORIGINS) { $env:ALLOWED_ORIGINS } else { "http://localhost:3000,http://127.0.0.1:3000" }
$env:COLLECTOR_ENABLED = if ($env:COLLECTOR_ENABLED) { $env:COLLECTOR_ENABLED } else { "false" }

mvn spring-boot:run

