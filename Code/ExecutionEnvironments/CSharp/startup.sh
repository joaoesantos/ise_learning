#!/bin/bash 
dotnet restore ./NetCoreExecutionEnvironment/NetCoreExecutionEnvironment.csproj 
dotnet run --project ./NetCoreExecutionEnvironment --configuration Release