package ru.hofftech.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.hofftech.model.Parcel;
import ru.hofftech.model.Truck;
import ru.hofftech.service.ParcelService;

import java.util.Arrays;
import java.util.List;

@ShellComponent
public class ShellCommands {

    private final ParcelService parcelService;

    public ShellCommands(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @ShellMethod(key = "find-all", value = "Show all parcels")
    public String findAll() {
        List<Parcel> parcels = parcelService.getAllParcelsList();
        if (parcels.isEmpty()) {
            return "No parcels found";
        }
        StringBuilder sb = new StringBuilder("Total parcels: " + parcels.size() + "\n");
        for (Parcel p : parcels) {
            sb.append("  ").append(p.getName()).append(" (").append(p.getWidth()).append("x").append(p.getHeight()).append(")\n");
        }
        return sb.toString();
    }

    @ShellMethod(key = "find", value = "Find parcel by name")
    public String find(@ShellOption String name) {
        try {
            Parcel p = parcelService.getParcelByName(name);
            StringBuilder sb = new StringBuilder();
            sb.append("Parcel: ").append(p.getName()).append("\n");
            sb.append("Symbol: ").append(p.getSymbol()).append("\n");
            sb.append("Size: ").append(p.getWidth()).append("x").append(p.getHeight()).append("\n");
            sb.append("Shape:\n");
            for (String line : p.getShape()) {
                sb.append("  ").append(line).append("\n");
            }
            return sb.toString();
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    @ShellMethod(key = "create", value = "Create parcel")
    public String create(
            @ShellOption({"-name", "--name"}) String name,
            @ShellOption({"-form", "--form"}) String form,
            @ShellOption({"-symbol", "--symbol"}) char symbol
    ) {
        List<String> shape = Arrays.asList(form.split("\\\\n"));
        try {
            parcelService.createParcel(name, shape, symbol);
            return "✅ Parcel '" + name + "' created";
        } catch (IllegalArgumentException e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @ShellMethod(key = "delete", value = "Delete parcel")
    public String delete(@ShellOption String name) {
        try {
            parcelService.deleteParcel(name);
            return "✅ Parcel '" + name + "' deleted";
        } catch (IllegalArgumentException e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @ShellMethod(key = "load", value = "Load parcels into trucks")
    public String load(
            @ShellOption({"-parcels", "--parcels"}) String parcels,
            @ShellOption({"-type", "--type"}) String type
    ) {
        List<String> names = Arrays.asList(parcels.split("\\\\n"));
        try {
            List<Truck> trucks = parcelService.loadParcels(names, type, 10);
            StringBuilder sb = new StringBuilder("Loaded " + trucks.size() + " trucks\n");
            for (int i = 0; i < trucks.size(); i++) {
                sb.append("Truck ").append(i + 1).append(":\n");
                sb.append(trucks.get(i).toString()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}