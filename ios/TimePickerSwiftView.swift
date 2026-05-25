import SwiftUI
import UIKit

// MARK: - Coordinator

class TimePickerCoordinator: NSObject {
    var parent: TimePickerUIViewRepresentable

    init(_ parent: TimePickerUIViewRepresentable) {
        self.parent = parent
    }

    @objc func dateChanged(_ picker: UIDatePicker) {
        parent.date = picker.date
    }
}

// MARK: - UIViewRepresentable

struct TimePickerUIViewRepresentable: UIViewRepresentable {
    @Binding var date: Date
    var minuteInterval: Int
    var locale: String
    var textColor: UIColor?

    func makeCoordinator() -> TimePickerCoordinator { TimePickerCoordinator(self) }

    func makeUIView(context: Context) -> UIDatePicker {
        let picker = UIDatePicker()
        picker.datePickerMode = .time
        picker.preferredDatePickerStyle = .wheels
        picker.minuteInterval = minuteInterval
        picker.locale = locale.isEmpty ? .current : Locale(identifier: locale)
        picker.addTarget(context.coordinator, action: #selector(TimePickerCoordinator.dateChanged(_:)), for: .valueChanged)
        applyStyle(to: picker)
        return picker
    }

    func updateUIView(_ picker: UIDatePicker, context: Context) {
        context.coordinator.parent = self
        picker.minuteInterval = minuteInterval
        picker.locale = locale.isEmpty ? .current : Locale(identifier: locale)
        if abs(picker.date.timeIntervalSince1970 - date.timeIntervalSince1970) > 1 {
            picker.setDate(date, animated: false)
        }
        // Must reapply after any picker update — UIDatePicker resets KVO values on redraw
        applyStyle(to: picker)
    }

    private func applyStyle(to picker: UIDatePicker) {
        if let color = textColor {
            picker.setValue(color, forKey: "textColor")
        }
    }
}

// MARK: - State

class TimePickerState: ObservableObject {
    @Published var minuteInterval: Int
    @Published var locale: String
    @Published var date: Date
    @Published var textColor: UIColor?
    var onValueChange: ((Double) -> Void)?

    init(minuteInterval: Int, locale: String, date: Date) {
        self.minuteInterval = minuteInterval
        self.locale = locale
        self.date = date
    }
}

// MARK: - SwiftUI view

struct TimePickerSwiftUIView: View {
    @ObservedObject var state: TimePickerState

    var body: some View {
        TimePickerUIViewRepresentable(
            date: $state.date,
            minuteInterval: state.minuteInterval,
            locale: state.locale,
            textColor: state.textColor
        )
        .onChange(of: state.date) { newDate in
            state.onValueChange?(newDate.timeIntervalSince1970 * 1000)
        }
    }
}

// MARK: - ObjC bridge

@objc public class TimePickerViewBridge: NSObject {
    private let hostingController: UIHostingController<TimePickerSwiftUIView>
    private let state: TimePickerState

    @objc public override convenience init() {
        self.init(minuteInterval: 1, locale: "", timestamp: Date().timeIntervalSince1970 * 1000)
    }

    @objc public init(minuteInterval: Int, locale: String, timestamp: Double) {
        let date = timestamp > 0 ? Date(timeIntervalSince1970: timestamp / 1000) : Date()
        state = TimePickerState(minuteInterval: minuteInterval, locale: locale, date: date)
        hostingController = UIHostingController(rootView: TimePickerSwiftUIView(state: state))
        hostingController.view.backgroundColor = .clear
        super.init()
    }

    @objc public var view: UIView { hostingController.view }

    @objc public func setMinuteInterval(_ value: Int)   { state.minuteInterval = value }
    @objc public func setLocale(_ value: String)        { state.locale = value }
    @objc public func setTextColor(_ value: UIColor)    { state.textColor = value }
    @objc public func setValue(_ timestamp: Double) {
        let newDate = Date(timeIntervalSince1970: timestamp / 1000)
        if abs(state.date.timeIntervalSince1970 - newDate.timeIntervalSince1970) > 1 {
            state.date = newDate
        }
    }
    @objc public func setOnChange(_ callback: @escaping (Double) -> Void) {
        state.onValueChange = callback
    }
}
